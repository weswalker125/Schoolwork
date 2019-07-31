using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Data.SqlClient;
using System.Text;

namespace CS505P2.Service
{
    /// <summary>
    /// Collection of functions which do no require object instantiation
    /// Author note: I tried to make all functions (possible) static, to increase 
    /// performance and instill simplicity in code-execution and reading.
    /// </summary>
    public static class StaticServices
    {
        /// <summary>
        /// Retrieve a connection to the database
        /// </summary>
        /// <returns></returns>
        private static SqlConnection GetConnection()
        {
            //Create a database connection
            SqlConnection conn = new SqlConnection(Constants.DB_CONNECTION_STRING);
            return conn;
        }

        /// <summary>
        /// Perform database interaction which does not require feedback.
        /// (Typically used for writing and deleting)
        /// </summary>
        /// <param name="nonquery">SQL query to perform</param>
        public static void ExecuteNonQuery(string nonquery)
        {
            //Get database connection
            SqlConnection conn = GetConnection();

            //Open connection
            conn.Open();

            //Execute query
            SqlCommand command = new SqlCommand(nonquery, conn);
            command.ExecuteNonQuery();

            //Close connection
            conn.Close();
        }

        /// <summary>
        /// Perform database interaction which returns a single number
        /// </summary>
        /// <param name="scalarQuery">SQL query to perform</param>
        /// <returns>Numerical result</returns>
        public static int ExecuteScalar(string scalarQuery)
        {
            string s_result = null;
            int i_result = 0;

            //Get database connection
            SqlConnection conn = GetConnection();

            //Open connection
            conn.Open();

            //Execute query
            SqlCommand command = new SqlCommand(scalarQuery, conn);
            s_result = command.ExecuteScalar().ToString();

            //Close connection
            conn.Close();

            if(!int.TryParse(s_result, out i_result))
                return -1;
            else
                return i_result;
        }

        /// <summary>
        /// Perform database interaction which returns a number of rows (with numerous fields)
        /// </summary>
        /// <param name="query">SQL query to perform</param>
        /// <returns>Each list of strings returned (in the list) represents a ROW in the results</returns>
        public static List<List<string>> ExecuteQuery(string query)
        {
            //Get database connection
            SqlConnection conn = GetConnection();

            //Open connection
            conn.Open();

            //Execute query
            SqlCommand command = new SqlCommand(query, conn);

            List<List<string>> results = new List<List<string>>();

            SqlDataReader sr = command.ExecuteReader();
            while (sr.Read())
            {
                List<string> result = new List<string>();

                //Get data
                object[] values = new object[Constants.MAX_COLUMNS];
                sr.GetValues(values);

                //Format into list:
                foreach (object i in values)
                {
                    try
                    {
                        if (i != null)
                        {
                            result.Add(i.ToString().Trim());
                        }
                    }
                    catch (Exception ex)
                    {
                        System.Diagnostics.Debug.WriteLine(ex.Message);
                        continue;
                    }
                }

                results.Add(result);
            }

            //Close connection
            conn.Close();

            return results;
        }

        /// <summary>
        /// Method to read in a given file, which represents a database (as per the description seen under 'documentation')
        /// Parse the data, create tablets (as described in the Project Assignment), and insert the appropriate data to the appropriate tablet.
        /// </summary>
        /// <param name="filename">Path to file</param>
        /// <param name="output">TESTING FLAG: when true the method does not make database interaction (just outputs the intended
        /// queries to the console.  (Set to false for standard use)</param>
        /// <returns>(TESTING RETURN) Information about the "table" which was created.  (What the table would've looked like in a database 
        /// without partitioning, for testing validity of the function)</returns>
        public static TableInfo PopulateTablets(string filename, bool output = false)
        {
            using (StreamReader sr = new StreamReader(filename))
            {
                int columnCt = 0;
                string fileContents = null, baseTableName = null;
                string[] records, fields, columns, types;
                Random rGen = null;

                fileContents = sr.ReadToEnd();
                records = fileContents.Split('*');

                //Check validity of file:
                if (records.Length <= 2)
                    throw new Exception("Invalid file format: must contain headers and data", null);

                //Remove newlines from all records
                for (int i = 0; i < records.Length; ++i)
                    records[i] = records[i].Trim();

                #region Headers
                //Get column names
                columns = records[0].Split(';');
            
                //Get types
                types = records[1].Split(';');

                //Check validity of headers
                if (columns.Length != types.Length)
                    throw new Exception("Invalid file format: must have equal number of column names as column types", null);

                //Store number of columns we expect
                columnCt = columns.Length;

                //Determine 'base table' name
                baseTableName = filename.Substring(filename.LastIndexOf('\\') + 1);
                baseTableName = baseTableName.Remove(baseTableName.LastIndexOf('.'));
                #endregion

                #region Create Tables
                for (int i = 0; i < columnCt; ++i)
                {
                    string query = Constants.CREATE_TABLET(baseTableName, columns[i], types[i]);
                    if(output)
                        //Print the CREATE QUERY
                        Console.WriteLine(query);
                    else
                        //Execute the CREATE QUERY
                        Service.StaticServices.ExecuteNonQuery(query);
                }
                #endregion

                #region Data
                rGen = new Random((int)DateTime.Now.ToFileTime());
                for (int i = 2; i < records.Length; ++i)
                {
                    try
                    {
                        if (string.IsNullOrEmpty(records[i]))
                            continue;

                        fields = records[i].Split(';');
                        //Validate number of data in record (matches number of columns)
                        if (fields.Length != columnCt)
                            throw new Exception(string.Format("Invalid file format: must have equal data length as column numbers\nError on record {0}", i + 1), null);

                        //Compute serial key:
                        int serialKey = rGen.Next();

                        for (int j = 0; j < fields.Length; ++j)
                        {
                            if(output)
                                //Print the query
                                Console.WriteLine(Constants.INSERT_RECORD(baseTableName, columns[j], serialKey, fields[j]));
                            else
                                //Insert this record into the database
                                ExecuteNonQuery(Constants.INSERT_RECORD(baseTableName, columns[j], serialKey, fields[j]));
                      }
                    }
                    catch (Exception ex)
                    {
                        System.Diagnostics.Debug.WriteLine(ex.Message);
                        continue;
                    }
                }
                #endregion

                TableInfo ret = new TableInfo() { baseTableName = baseTableName, columnNames = columns, columnTypes = types };
                return ret;
            }
        }

        /// <summary>
        /// Method to comprehend what the user's desired results should be and formating a new 
        /// query which will provide the desired results
        /// </summary>
        /// <param name="userQuery">Standard SQL query given by the user (who assumes we're operating on a lone table)</param>
        public static string ParseUserQuery(string userQuery)
        {
            #region Basic information (SELECT, FROM)
            QueryInfo info = new QueryInfo();
            
            //Get columns in SELECT clause
            string columns = userQuery.Substring(7, (userQuery.IndexOf("FROM") - 7)).Replace(" ", "");
            string[] sa_columns = columns.Split(',');
            foreach (string col in sa_columns)
                info.Select.Add(col);

            //Get table name:
            info.From = userQuery.Substring((userQuery.IndexOf("FROM") + 5), ((userQuery.IndexOf("WHERE")) - (userQuery.IndexOf("FROM") + 5))).Trim();
            #endregion 

            #region Complex information (WHERE)
            //Extract where clause
            string wheres = userQuery.Substring(userQuery.IndexOf("WHERE") + 6);
            //List<Condition> conditions = new List<Condition>();
            //List<string> junctions = new List<string>();

            //Separate each clause (parenthesised)
            for (int i = 0; i < wheres.Length; ++i)
            {
                //Start of clause
                if (wheres[i] == '(')
                {
                    int start = i + 1, end;
                    //Get length of clause
                    while (wheres[i] != ')')
                        ++i;
                    end = i;

                    string s_condition = wheres.Substring(start, (end - start));
                    int op_index = 0;

                    Condition condition = new Condition();
                    //Find the operator and its location within the string
                    foreach (string op in Constants.operators)
                        if ((op_index = s_condition.IndexOf(op)) != -1)
                        {
                            condition.Operator = op;
                            break;
                        }

                    //Get left and right hand sides (respectively)
                    condition.LHS = s_condition.Substring(0, op_index).Trim();
                    condition.RHS = s_condition.Substring(s_condition.IndexOf(condition.Operator) + condition.Operator.Length).Trim();
    
                    info.Where.Add(condition);
                    
                    //Read for junction
                    i = end;
                    while(i < wheres.Length && wheres[i] != '(')
                        i++;
                    if (i >= wheres.Length)
                        break;

                    string potentialJunction = wheres.Substring(end, (i - end) + 1);
                    foreach(string junct in Constants.junctions)
                        if (potentialJunction.Contains(junct))
                        {
                            info.WhereJunctions.Add(junct);
                            break;
                        }
                    i = i - 1;
                }
            }
            #endregion

            //Validation
            if (info.WhereJunctions.Count != info.Where.Count - 1)
                throw new Exception("Error in input query: Could not detect proper number of junctions (AND/OR)", null);

            #region Create Transformed Query
            string query = "SELECT {0} FROM {1} WHERE {2}";
            TransformedQueryInfo tQuery = new TransformedQueryInfo();

            //Adjust selects 
            tQuery.Select = "";
            for (int k = 0; k < info.Select.Count; ++k)
                tQuery.Select += string.Format("{0}_{1}.{1}, ", info.From, info.Select[k]);
            tQuery.Select = tQuery.Select.Remove(tQuery.Select.LastIndexOf(','), 1).Trim();
            

            //Configure all "From" tables
            List<string> LHSs = new List<string>();
            tQuery.Where = "(";
            for (int j = 0; j < info.Where.Count; ++j)
            {
                if (!LHSs.Contains(info.Where[j].LHS))
                    LHSs.Add(info.Where[j].LHS);

                //Adjust LHS table names
                string newLhs = string.Format("{0}_{1}.{1}", info.From, info.Where[j].LHS);
                tQuery.Where += string.Format("({0} {1} {2}) ", newLhs, info.Where[j].Operator, info.Where[j].RHS);
                if(j < info.Where.Count - 1)
                    tQuery.Where += info.WhereJunctions[j] + " ";
            }
            tQuery.Where += ")";

            string last = "";
            
            //Add selects which aren't include in LHSs to that list (tables we must choose from)
            for (int s = 0; s < info.Select.Count; ++s)
            {
                if (!LHSs.Contains(info.Select[s]))
                    LHSs.Add(info.Select[s]);
                
                if (s == 0)
                    last = info.Select[s];
                else
                {
                    tQuery.Where += " AND (" + info.From + "_" + info.Select[s] + ".serial = " + info.From + "_" + last + ".serial)";
                    last = info.Select[s];
                }
            }

            string from = "";
            foreach (string lhs in LHSs)
                from += string.Format("{0}_{1}, ", info.From, lhs);
            tQuery.From = from.Remove(from.LastIndexOf(','), 1).Trim();

            string transformedQuery = string.Format(query, tQuery.Select, tQuery.From, tQuery.Where);
            transformedQuery += " GROUP BY " + tQuery.Select;
            #endregion

            return transformedQuery;
        }
    }

    //Non-static service layer
    public class Services
    {
        //Todo, if needed
    }
}
