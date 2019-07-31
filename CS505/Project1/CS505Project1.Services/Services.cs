using System;
using System.Collections.Generic;
//using System.Data.SqlServerCe;
using System.Data.SqlClient;
using System.Linq;
using System.Text;

namespace CS505Project1.Services
{
    public class StandardService
    {
        #region Constant string values and custom queries
        const string info_DATABASECONNECTION = "Server=DATAXCORE;Database=CS505DB;User Id=sa;Password=lookslikeyou12;";

        const string query_GETTABLENAMES = "USE CS505DB SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_TYPE = 'BASE TABLE' AND TABLE_CATALOG = 'CS505DB'";
        const string query_GETUSERID = "SELECT id FROM [CS505DB].[dbo].[user] WHERE user_name='{0}'";
        const string query_GETUSERNAMES = "SELECT user_name FROM [CS505DB].[dbo].[user]";
        const string query_GETTABLE = "SELECT * FROM [CS505DB].[dbo].[{0}]";
        const string query_GETCOLUMNNAMES = "select [COLUMN_NAME] from information_schema.columns where table_name = '{0}' order by ordinal_position";
        const string query_PERMISSIONEXISTS = "SELECT COUNT(*) FROM [CS505DB].[dbo].[my_permissions] WHERE user_id={0} AND table_name='{1}' AND operation>={2} AND grant_option>={3}";
        const string query_GENERICINSERT = "INSERT INTO [CS505DB].[dbo].[{0}] VALUES('{1}', '{2}', '{3}')";
        const string query_MYPERMISSIONS = "SELECT * FROM [CS505DB].[dbo].[my_permissions] WHERE user_id={0}";

        const string query_FORBIDDENRULE_EXISTS = "SELECT COUNT(*) FROM [CS505DB].[dbo].[forbidden] WHERE user_id={0} AND table_name='{1}' AND operation>={2} AND grant_option>={3}";
        const string query_PERMITTEDRULE_EXISTS = "SELECT COUNT(*) FROM [CS505DB].[dbo].[permitted] WHERE user_id={0} AND table_name='{1}' AND operation>={2} AND grant_option>={3}";

        #region Admin queries
        public const string query_GETFORBIDDENLIST = "SELECT * FROM [CS505DB].[dbo].[forbidden]";
        public const string query_GETPERMITTEDLIST = "SELECT * FROM [CS505DB].[dbo].[permitted]";

        const string query_ONFORBIDDENLIST = "SELECT id FROM [CS505DB].[dbo].[forbidden] WHERE user_id={0} AND table_name={1} AND operation={2}";
        const string query_ONPERMITTEDLIST = "SELECT id FROM [CS505DB].[dbo].[permitted] WHERE user_id={0} AND table_name={1} AND operation={2}";

        const string query_ADDTOPERMITTED = "INSERT INTO [CS505DB].[dbo].[permitted] VALUES({0}, '{1}', '{2}', {3})";
        const string query_ADDTOFORBIDDEN = "INSERT INTO [CS505DB].[dbo].[forbidden] VALUES({0}, '{1}', '{2}', {3})";

        const string delete_REMOVEFROMPERMITTED = "DELETE FROM [CS505DB].[dbo].[permitted] WHERE user_id={0} AND table_name='{1}' AND operation={2} AND grant_option={3}";
        const string delete_REMOVEFROMFORBIDDEN = "DELETE FROM [CS505DB].[dbo].[forbidden] WHERE user_id={0} AND table_name='{1}' AND operation={2} AND grant_option={3}";
        #endregion

        #region Table actions
        const string query_ISPERMITTEDTOINSERT = "SELECT COUNT(*) FROM [CS505DB].[dbo].[my_permissions] WHERE user_id={0} AND table_name='{1}' AND operation=1";
        const string query_ISPERMITTEDTOSELECT = "SELECT COUNT(*) FROM [CS505DB].[dbo].[my_permissions] WHERE user_id={0} AND table_name='{1}'";
        const string query_ISFORBIDDENTOINSERT = "SELECT COUNT(*) FROM [CS505DB].[dbo].[forbidden] WHERE user_id={0} AND table_name='{1}' AND operation=1 AND grant_option=0";
        const string query_ISFORBIDDENTOSELECT = "SELECT COUNT(*) FROM [CS505DB].[dbo].[forbidden] WHERE user_id={0} AND table_name='{1}' AND grant_option=0";
        #endregion

        #region Grant actions
        const string query_ISALLOWEDTOGRANT_INSERT = "SELECT COUNT(*) FROM [CS505DB].[dbo].[my_permissions] WHERE user_id={0} AND table_name='{1}' AND operation=1 AND grant_option=1";
        const string query_ISALLOWEDTOGRANT_SELECT = "SELECT COUNT(*) FROM [CS505DB].[dbo].[my_permissions] WHERE user_id={0} AND table_name='{1}' AND grant_option=1";
        
        const string query_CANUSERBEGRANTED_SELECT = "SELECT COUNT(*) FROM [CS505DB].[dbo].[permitted] WHERE user_id={0} AND table_name='{1}'";
        const string query_CANUSERBEGRANTED_INSERT = "SELECT COUNT(*) FROM [CS505DB].[dbo].[permitted] WHERE user_id={0} AND table_name='{1}' AND operation=1";
        const string query_CANUSERBEGRANTED_SELECT_WITHGRANT = "SELECT COUNT(*) FROM [CS505DB].[dbo].[permitted] WHERE user_id={0} AND table_name='{1}' AND grant_option=1";
        const string query_CANUSERBEGRANTED_INSERT_WITHGRANT = "SELECT COUNT(*) FROM [CS505DB].[dbo].[permitted] WHERE user_id={0} AND table_name='{1}' AND operation=1 AND grant_option=1";

        const string insert_GRANT = "INSERT INTO [CS505DB].[dbo].[my_permissions] VALUES('{0}', '{1}', '{2}', '{3}')";
        const string delete_REVOKE = "DELETE FROM [CS505DB].[dbo].[my_permissions] WHERE user_id={0} AND table_name='{1}' AND operation={2} AND grant_option={3}";

        const string query_ISFORBIDDENTOGRANTINSERT = "SELECT COUNT(*) FROM [CS505DB].[dbo].[forbidden] WHERE user_id={0} AND table_name='{1}' AND operation=1 AND grant_option=1";
        const string query_ISFORBIDDENTOGRANTSELECT = "SELECT COUNT(*) FROM [CS505DB].[dbo].[forbidden] WHERE user_id={0} AND table_name='{1}' AND grant_option=1";

        const string query_ISFORBIDDENTOBEGRANTEDINSERT = "SELECT COUNT(*) FROM [CS505DB].[dbo].[forbidden] WHERE user_id={0} AND table_name='{1}' AND operation=1 AND grant_option=0";
        const string query_ISFORBIDDENTOBEGRANTEDSELECT = "SELECT COUNT(*) FROM [CS505DB].[dbo].[forbidden] WHERE user_id={0} AND table_name='{1}' AND grant_option=0";
        #endregion

        #endregion 

        //SqlCeConnection _connection = null;
        SqlConnection _connection = null;

        public StandardService()
        {
            //Connection to Database
            _connection = new SqlConnection(info_DATABASECONNECTION);
        }

        public List<string> GetColumnNames(string tableName)
        {
            return GetRecords(string.Format(query_GETCOLUMNNAMES, tableName), '\t');
        }

        public List<List<string>> GetForbiddenTable()
        {
            return GetMultiColumnRecords(query_GETFORBIDDENLIST);
        }

        public List<List<string>> GetMultiColumnRecords(string query)
        {
            List<string> original = GetRecords(query);
            List<List<string>> ret = new List<List<string>>();

            foreach (string o in original)
            {
                string[] sa_records = o.Split(';');
                List<string> l_records = new List<string>();

                foreach (string r in sa_records)
                    l_records.Add(r);

                ret.Add(l_records);
            }

            return ret;
        }

        public List<string> GetTable(string tableName)
        {

            return GetRecords(string.Format(query_GETTABLE, tableName), '\t');
        }

        /// <summary>
        /// GET RECORDS
        ///     Given a query (SELECT statement) a connection to the database is opened,
        ///     the query is executed and the results are returned.
        /// </summary>
        /// <param name="selectQuery">SQL statement to execute on the database.</param>
        /// <param name="delimiter">character to separate column values in one record</param>
        /// <returns>List of strings, each string represented one row/record, columns in that record are delimitted
        /// by the delimiter parameter</returns>
        public List<string> GetRecords(string selectQuery, char delimiter = ';')
        {
            List<string> records = new List<string>();

            //Open database connection
            _connection.Open();
            //Prepare query
            SqlCommand cmd = new SqlCommand(selectQuery, _connection);

            //Execute the query
            using (SqlDataReader sr = cmd.ExecuteReader())
            {
                //Read each row
                while (sr.Read())
                {
                    //Get all values returned per row
                    string row = string.Empty;
                    if (sr.FieldCount == 1) //if only one column, no need for delimitting
                    {
                        row += sr.GetValue(0).ToString().Trim();
                        row = row.Trim();
                    }
                    else
                    {
                        for (int i = 0; i < sr.FieldCount; ++i)
                        {
                            row += sr.GetValue(i).ToString().Trim();
                            row += delimiter;
                        }
                    }
                    //Push record onto list of records
                    records.Add(row);
                }
            }

            //Close connection
            _connection.Close();

            return records;
        }
        
        public bool AddPermissOrForbid(CS505Project1.Domains.Permission permission)
        {
            //Check than an entry isn't conflicting with another entry
            if (IsConflicting(permission))
                return false;

            int userId = GetUserId(permission.user_name);
            if (userId == 0)
                return false; //User was not found 
            
            try
            {
                if (permission.type == Domains.Permission_Type.FORBIDDEN)
                {
                    //Check that this isn't already in forbidden table
                    string check_query = string.Format(query_FORBIDDENRULE_EXISTS, userId, permission.table_name, BoolToInt(permission.write), BoolToInt(permission.grant));
                    List<string> result = GetRecords(check_query);
                    if (result.Count != 0 && !string.IsNullOrEmpty(result[0]) && (result[0] != "0"))
                        throw new Exception("Attempted grant permission already exists or is superseded by a more powerful permission", null);

                    NonQuery(string.Format(query_ADDTOFORBIDDEN, userId, permission.table_name, BoolToInt(permission.write), BoolToInt(permission.grant)));
                }
                else
                {
                    //Check that this isn't already in forbidden table
                    string check_query = string.Format(query_PERMITTEDRULE_EXISTS, userId, permission.table_name, BoolToInt(permission.write), BoolToInt(permission.grant));
                    List<string> result = GetRecords(check_query);
                    if (result.Count != 0 && !string.IsNullOrEmpty(result[0]) && (result[0] != "0"))
                        throw new Exception("Attempted grant permission already exists or is superseded by a more powerful permission", null);

                    NonQuery(string.Format(query_ADDTOPERMITTED, userId, permission.table_name, BoolToInt(permission.write), BoolToInt(permission.grant)));
                }
            }
            catch (Exception ex)
            {
                if (_connection.State == System.Data.ConnectionState.Open)
                    _connection.Close();
                throw ex;
            }
            return true;
        }

        public bool RemoveFromPermissionOrForbidden(bool permittedTable, int user_id, string table_name, bool operation, bool grant)
        {
            try
            {
                if (permittedTable)
                {
                    string query = string.Format(delete_REMOVEFROMPERMITTED, user_id, table_name, BoolToInt(operation), BoolToInt(grant));
                    NonQuery(query);
                    return true;
                }
                else
                {
                    string query = string.Format(delete_REMOVEFROMFORBIDDEN, user_id, table_name, BoolToInt(operation), BoolToInt(grant));
                    NonQuery(query);
                    return true;
                }
            }
            catch (Exception ex)
            {
                throw ex;
            }
        }

        /// <summary>
        /// CREATEUSER
        ///     Makes a new user in the user table
        /// </summary>
        /// <param name="user_name">log-in name</param>
        /// <param name="first_name"> first name</param>
        /// <param name="last_name">last name</param>
        public void CreateUser(string user_name, string first_name, string last_name)
        {
            NonQuery(string.Format("INSERT INTO [user] (user_name, first_name, last_name) VALUES('{0}', '{1}', '{2}')", user_name, first_name, last_name));
        }

        /// <summary>
        /// GETALLUSERNAMES
        ///     Retrieves list of user log-in names (used to populate
        ///     lists/forms which list all users)
        /// </summary>
        /// <returns></returns>
        public List<string> GetAllUserNames()
        {
            return GetRecords(query_GETUSERNAMES);
        }

        /// <summary>
        /// GETALLOWEDTABLENAMES
        ///     Similar to GetAllTableNames(), except we remove tables which shouldn't be seen by a
        ///     standard user.
        /// </summary>
        /// <returns></returns>
        public List<string> GetAllowedTableNames()
        {
            List<string> allTables = GetAllTableNames();

            //Remove the permissions tables (ONLY CSO SHOULD EVER PERMISS TO SEE THESE)
            allTables.Remove("forbidden");
            allTables.Remove("permitted");
            allTables.Remove("my_permissions");

            return allTables;
        }

        /// <summary>
        /// GETALLTABLENAMES
        ///     Retreives ALL tables in this program's database.
        /// </summary>
        /// <returns>List of names (strings) of tables</returns>
        public List<string> GetAllTableNames()
        {
            return GetRecords(query_GETTABLENAMES);
        }

        ///Verifies that a table action can be performed (if it cannot be performed
        ///either due to it not being permitted, or worse, it is forbidden, we throw an exception
        ///telling the user the reason for revoking the action).
        public void CanPerformTableAction(CS505Project1.Domains.TableAction tableAction)
        {
            if (IsPermitted(tableAction))
            {
                return;
            }
            else
            {
                //Check forbidden list
                if (IsForbidden(tableAction))
                {
                    //Todo: LOG or notify the DBA or CSO
                    throw new Exception("ERROR: This action has been FORBIDDEN for this user.\nTHIS ATTEMPT HAS BEEN REPORTED TO THE DBA/CSO", null);
                }
                else
                    throw new Exception("Sorry: User is not permitted to perform this action.", null);
            }
        }

        public void PerformGenericInsert(string tableName, string value1, string value2, string value3)
        {
            string query = string.Format(query_GENERICINSERT, tableName, value1, value2, value3);
            NonQuery(query);
        }

        private bool IsForbidden(CS505Project1.Domains.TableAction tableAction)
        {
            //Get UserID:
            int userId = GetUserId(tableAction.user_name);

            //If attempting insert:
            if (tableAction.operation == Domains.Operation_Type.INSERT)
            {
                string query = string.Format(query_ISFORBIDDENTOINSERT, userId, tableAction.table_name);
                List<string> result = GetRecords(query);
                if (result.Count == 0 || string.IsNullOrEmpty(result[0]) || (result[0] == "0"))
                    return false; //not forbidden
                else return true; 
            }
            else if (tableAction.operation == Domains.Operation_Type.SELECT)
            {
                string query = string.Format(query_ISFORBIDDENTOSELECT, userId, tableAction.table_name);
                List<string> result = GetRecords(query);
                if (result.Count == 0 || string.IsNullOrEmpty(result[0]) || (result[0] == "0"))
                    return false; //not forbidden
                else return true;

            }
            return true;
        }

        private bool IsForbidden(CS505Project1.Domains.GrantAction grantAction)
        {
            //Get UserID:
            int userId_grantor = GetUserId(grantAction.grantor_name);
            int userId_grantee = GetUserId(grantAction.grantee_name);

            //If attempting insert:
            if (grantAction.operation == Domains.Operation_Type.INSERT)
            {
                string query = string.Format(query_ISFORBIDDENTOGRANTINSERT, userId_grantor, grantAction.table_name);
                List<string> result = GetRecords(query);
                if (result.Count == 0 || string.IsNullOrEmpty(result[0]) || (result[0] == "0"))
                    return false; //not forbidden
                    else return true;
            }
            else if (grantAction.operation == Domains.Operation_Type.SELECT)
            {
                string query = string.Format(query_ISFORBIDDENTOGRANTSELECT, userId_grantor, grantAction.table_name);
                List<string> result = GetRecords(query);
                if (result.Count == 0 || string.IsNullOrEmpty(result[0]) || (result[0] == "0"))
                    return false; //not forbidden
                else return true;
            }
            return true;
        }

        private bool IsPermitted(CS505Project1.Domains.TableAction tableAction)
        {
            //CurrentUser tries to read/write to a table
            //Form permission around attempted action:
                //They want to READ from "clients"
                //check in permitted table for user_name='CurrentUser' and table_name='clients'
            
            //Get UserID:
            int userId = GetUserId(tableAction.user_name);

            //If attempting insert:
            if(tableAction.operation == Domains.Operation_Type.INSERT)
            {
                string query = string.Format(query_ISPERMITTEDTOINSERT, userId, tableAction.table_name);
                List<string> result = GetRecords(query);
                if (result.Count == 0 || string.IsNullOrEmpty(result[0]) || (result[0] == "0"))
                    return false; //not permitted
                else return true;
            }
            else if (tableAction.operation == Domains.Operation_Type.SELECT)
            {
                string query = string.Format(query_ISPERMITTEDTOSELECT, userId, tableAction.table_name);
                List<string> result = GetRecords(query);
                if (result.Count == 0 || string.IsNullOrEmpty(result[0]) || (result[0] == "0"))
                    return false; //not permitted
                else return true;

            }
            return false;
        }

        private bool IsPermitted(CS505Project1.Domains.GrantAction grantAction)
        {
            //Get UserID:
            int userId_grantor = GetUserId(grantAction.grantor_name);
            int userId_grantee = GetUserId(grantAction.grantee_name);

            //If attempting insert:
            if (grantAction.operation == Domains.Operation_Type.INSERT)
            {
                string query = string.Format(query_ISALLOWEDTOGRANT_INSERT, userId_grantor, grantAction.table_name);
                List<string> result = GetRecords(query);
                if (result.Count == 0 || string.IsNullOrEmpty(result[0]) || (result[0] == "0"))
                    return false; //not permitted
                else return true;
            }
            else if (grantAction.operation == Domains.Operation_Type.SELECT)
            {
                string query = string.Format(query_ISALLOWEDTOGRANT_SELECT, userId_grantor, grantAction.table_name);
                List<string> result = GetRecords(query);
                if (result.Count == 0 || string.IsNullOrEmpty(result[0]) || (result[0] == "0"))
                    return false; //not permitted
                else return true;
            }
            return false;
        }

        /// <summary>
        /// ISCONFLICTING
        ///     Checks that a suggested permission (either permit or forbid) doesn't conflict with an existing entry
        /// </summary>
        /// <param name="permission"></param>
        /// <returns>TRUE if conflict exists.  False if this is a completely legal action</returns>
        private bool IsConflicting(CS505Project1.Domains.Permission permission)
        {
            //Get UserID:
            int userId = GetUserId(permission.user_name);
            
            //Using a grantAction object because it's the same idea, and Admin has all permissions permitted
            Domains.GrantAction grantAction = new Domains.GrantAction() { grantor_name = "admin", grantee_name = permission.user_name, table_name = permission.table_name, grant = permission.grant };
            if (permission.write)
                grantAction.operation = Domains.Operation_Type.INSERT;
            else
                grantAction.operation = Domains.Operation_Type.SELECT;

            //Check if permitting or forbidding an action:
            if (permission.type == Domains.Permission_Type.PERMITTED)
            {
                //Check that this isn't already forbidden:
                if (IsGrantForbidden(grantAction))
                {
                    throw new Exception("CONFLICT while attempting to permit action!", null);
                    return true; //conflict
                }
                else return false;
            }
            else
            {
                if (IsPermitted(grantAction))
                {
                    throw new Exception("CONFLICT while attempting to forbid action!", null);
                    return true;    //conflict
                }
                else return false;
            }
        }

        /// <summary> Low-Level
        /// GETUSERID
        ///     Given a string (representing a user in our database), query the
        ///     user table for the corresponding id.  Using the id makes the data across
        ///     tables more consistent.
        /// </summary>
        /// <param name="user_name">string of user's log-in name to lookup</param>
        /// <returns>corresponding id in the user table</returns>
        private int GetUserId(string user_name)
        {
            _connection.Open();
            string query = string.Format(query_GETUSERID, user_name);
            SqlCommand cmd = new SqlCommand(query, _connection);

            //Execute the query
            int ret = 0; //convert result from string to integer
            int.TryParse(cmd.ExecuteScalar().ToString(), out ret);

            _connection.Close();

            return ret;
        }

        /// <summary> Low-Level
        /// NONQUERY
        ///     Function to execute the given sql statement with no
        ///     regard for a returned/printed result.  Typically used to
        ///     INSERT or DELETE
        /// </summary>
        /// <param name="query">SQL statement</param>
        private void NonQuery(string query)
        {
            _connection.Open();
            SqlCommand cmd = new SqlCommand(query, _connection);

            int rowsAffected = 0;
            rowsAffected = cmd.ExecuteNonQuery();

            if (rowsAffected < 1)
            {
                _connection.Close();
                throw new Exception(string.Format("Error performing insert: {0}", query), null);
            }
            _connection.Close();
        }

        /// <summary>
        /// BOOLTOINT
        ///     Simple conversion (don't know of the [probably existing] call in C#)
        ///     atleast this reduces ~4 lines into 1 when called!
        /// </summary>
        /// <param name="value"></param>
        /// <returns></returns>
        private int BoolToInt(bool value)
        {
            //true
            if (value)
                return 1;
            else //false
                return 0;
        }

        /// <summary>
        /// MYPERMISSIONS
        ///     Function which retrieves the my_permissions table entries which belong to the given user
        /// </summary>
        /// <param name="userName">log in value for the current user</param>
        /// <returns>List of strings (one string per record, columns separated by tabs)</returns>
        public List<string> MyPermissions(string userName)
        {
            int userId = GetUserId(userName);

            string query = string.Format(query_MYPERMISSIONS, userId);

            return GetRecords(query, '\t');

        }


        /// <summary>
        /// GRANT attempts to make an entry in the my_permissions table, if their are rules which say it can be done
        ///     1. Check that the USER TRYING TO PERFORM GRANT can infact do so (via my_permissions)
        ///     2. Check that the USER BEING GRANTED the action is permitted to (via permitted_list)
        ///     3. PERFORMS GRANT
        /// If either of those checks fail, we reject the action, first we much check if this is a reportable incident
        ///     1. Check that the USER IS FORBIDDEN TO PERFORM GRANT on this table (via forbidden_list)
        ///     2. Check that the USER BEING GRANTED is FORBIDDEN TO HAVE THIS PRIVILEGE (via forbidden_list)
        /// If forbidden, an exception is throw (and handled on the UI level of the program)
        /// otherwise, simply do not perform the action.
        /// </summary>
        /// <param name="grantAction">Suggested action to perform (includes the user ATTEMPTING TO GRANT, the user BEING GRANTED, 
        /// the table to give permission on, and the rights for the user (being granted) to pass this on to others</param>
        public void Grant(Domains.GrantAction grantAction)
        {
            //Check if this is allowed and permitted
            //IF SO, PERFORM THE GRANT
            if (IsGrantPermitted(grantAction))
            {
                int userId_grantee = 0, operation = 0, grant = 0;
                string query = string.Empty;

                //Get the ID of the user being granted access
                userId_grantee = GetUserId(grantAction.grantee_name);

                //Convert value to boolean (int) (select == 0, insert == 1)
                if (grantAction.operation == Domains.Operation_Type.INSERT)
                    operation = 1;
                //Convert grant value to boolean (int)
                if (grantAction.grant)
                    grant = 1;

                //Format query
                query = string.Format(insert_GRANT, userId_grantee, grantAction.table_name, operation, grant);
                
                //Check that this isn't already in my_permissions
                string check_query = string.Format(query_PERMISSIONEXISTS, userId_grantee, grantAction.table_name, operation, grant);
                List<string> result = GetRecords(check_query);
                if (result.Count != 0 && !string.IsNullOrEmpty(result[0]) && (result[0] != "0"))
                    throw new Exception("Attempted grant permission already exists or is superseded by a more powerful permission", null);

                //Add this user/table/action to my_permissions!
                NonQuery(query);
                return;
            }
            else if (IsGrantForbidden(grantAction))
            {
                //Do NOT perform action.
                //Format exception message to throw
                string exMessage = string.Format("FORBIDDEN ACTION ATTEMPTED!\n[{0}] attempted <{1}, {2}, {3}, {4}>", 
                    grantAction.grantor_name, 
                    grantAction.grantee_name, 
                    grantAction.table_name, 
                    grantAction.operation.ToString(), 
                    grantAction.grant.ToString());

                throw new Exception(exMessage, null);
            }
            else
            {
                throw new Exception("Sorry, the requested action is not permitted", null);
            }
            
        }

        /// <summary>
        /// ISGRANTPERMITTED 
        ///     1. Checks that the user ATTEMPTING TO GRANT has permission to do so (via my_permissions table)
        ///     2. Checks that the user BEING GRANTED is allowed to have such an action (via permitted_list)
        /// </summary>
        /// <param name="grantAction">Suggested action to perform (includes the user ATTEMPTING TO GRANT, the user BEING GRANTED, 
        /// the table to give permission on, and the rights for the user (being granted) to pass this on to others</param>
        /// <returns>TRUE if both conditions are met.  FALSE if either condition is not met</returns>
        public bool IsGrantPermitted(CS505Project1.Domains.GrantAction grantAction)
        {
            int userId_grantor = 0, userId_grantee = 0;
            string query = string.Empty;
            List<string> result = null;

            //Get user IDs for the grantor and grantee
            userId_grantor = GetUserId(grantAction.grantor_name);
            userId_grantee = GetUserId(grantAction.grantee_name);
            if (userId_grantor == 0 || userId_grantee == 0)
                return false; //User was not found

            
            //Check that the user has the grant option in MY_PERMISSIONS for this table and action
            if(grantAction.operation == Domains.Operation_Type.SELECT)
                query = string.Format(query_ISALLOWEDTOGRANT_SELECT, userId_grantor, grantAction.table_name);
            else
                query = string.Format(query_ISALLOWEDTOGRANT_INSERT, userId_grantor, grantAction.table_name);

            result = GetRecords(query);
            if (result.Count == 0 || string.IsNullOrEmpty(result[0]) || (result[0] == "0"))
                return false; //not in my_permissions, return false
            
            ///////////////////////////////////
            //The user has the grant permission
            //check that the grantee is permitted to have this action
            query = null;
            if (grantAction.operation == Domains.Operation_Type.SELECT)
            {
                if (grantAction.grant)
                    query = string.Format(query_CANUSERBEGRANTED_SELECT_WITHGRANT, userId_grantee, grantAction.table_name);
                else
                    query = string.Format(query_CANUSERBEGRANTED_SELECT, userId_grantee, grantAction.table_name);
            }
            else
            {
                if(grantAction.grant)
                    query = string.Format(query_CANUSERBEGRANTED_INSERT_WITHGRANT, userId_grantee, grantAction.table_name);
                else
                    query = string.Format(query_CANUSERBEGRANTED_INSERT, userId_grantee, grantAction.table_name);
            }

            result = null;
            result = GetRecords(query);
            if (result.Count == 0 || string.IsNullOrEmpty(result[0]) || (result[0] == "0"))
                return false; //Not on the permitted list, return false

            //Found on the permitted list
            return true;
        }

        /// <summary>
        /// ISGRANTFORBIDDEN
        ///     1. Check that the USER IS FORBIDDEN TO PERFORM GRANT on this table (via forbidden_list)
        ///     2. Check that the USER BEING GRANTED is FORBIDDEN TO HAVE THIS PRIVILEGE (via forbidden_list)
        /// </summary>
        /// <param name="grantAction">Suggested action to perform (includes the user ATTEMPTING TO GRANT, the user BEING GRANTED, 
        /// the table to give permission on, and the rights for the user (being granted) to pass this on to others</param>
        /// <returns>TRUE if either condition is met.  FALSE if neither condition is met.</returns>
        public bool IsGrantForbidden(CS505Project1.Domains.GrantAction grantAction)
        {
            int userId_grantor = 0, userId_grantee = 0;
            string query = string.Empty;
            List<string> result = null;

            //Get user IDs for the grantor and grantee
            userId_grantor = GetUserId(grantAction.grantor_name);
            userId_grantee = GetUserId(grantAction.grantee_name);
            if (userId_grantor == 0 || userId_grantee == 0)
                return false; //User was not found

            //Check to see if the grantor is forbidden to grant on this table
            if(grantAction.operation == Domains.Operation_Type.SELECT)
                query = string.Format(query_ISFORBIDDENTOGRANTSELECT, userId_grantor, grantAction.table_name);
            else
                query = string.Format(query_ISFORBIDDENTOGRANTINSERT, userId_grantor, grantAction.table_name);

            result = GetRecords(query);
            if (result.Count != 0 && !string.IsNullOrEmpty(result[0]) && (result[0] != "0"))
                return true; //on forbidden_list, return true (IS FORBIDDEN)

            //Check to see if the grantee is forbidden to have this action
            query = string.Empty;
            result = null;
            if (grantAction.operation == Domains.Operation_Type.SELECT)
                query = string.Format(query_ISFORBIDDENTOBEGRANTEDSELECT, userId_grantee, grantAction.table_name);
            else
                query = string.Format(query_ISFORBIDDENTOBEGRANTEDINSERT, userId_grantee, grantAction.table_name);

            result = GetRecords(query);
            if (result.Count != 0 && !string.IsNullOrEmpty(result[0]) && (result[0] != "0"))
                return true; //on forbidden_list, return true (IS FORBIDDEN)

            //Neither were forbidden
            return false;
        }


        /// <summary>
        /// REVOKE
        ///     1. Provided the user trying to revoke someone's access has the raise privilege to do so,
        ///     we remove the permission from the my_permissions table
        /// </summary>
        /// <param name="revokeAction">Suggested action to perform (includes the user ATTEMPTING TO REVOKE, the user BEING REVOKED, 
        /// the table to remove permission on, and the rights for the user (being revoked)</param>
        public void Revoke(Domains.GrantAction revokeAction)
        {
            int userId_grantor = 0, userId_grantee = 0;
            string query = string.Empty;
            List<string> result = null;

            //Get user IDs for the grantor and grantee
            userId_grantor = GetUserId(revokeAction.grantor_name);
            userId_grantee = GetUserId(revokeAction.grantee_name);
            if (userId_grantor == 0 || userId_grantee == 0)
                return; //User was not found


            //Check that the user is permitted to grant (implies permission to revoke)
            if (revokeAction.operation == Domains.Operation_Type.SELECT)
                query = string.Format(query_ISALLOWEDTOGRANT_SELECT, userId_grantor, revokeAction.table_name);
            else
                query = string.Format(query_ISALLOWEDTOGRANT_INSERT, userId_grantor, revokeAction.table_name);

            result = GetRecords(query);
            if (result.Count == 0 || string.IsNullOrEmpty(result[0]) || (result[0] == "0"))
                throw new Exception("ERROR: USER NOT PERMITTED TO REVOKE", null); //not in my_permissions

            query = null;
            result = null;
            //User is allowed to grant to others on that table
            //Proceed to revoking the action
            if (revokeAction.operation == Domains.Operation_Type.SELECT)
            {
                if (revokeAction.grant)
                    query = string.Format(delete_REVOKE, userId_grantee, revokeAction.table_name, 0, 1);
                else
                    query = string.Format(delete_REVOKE, userId_grantee, revokeAction.table_name, 0, 0);
            }
            else
            {
                if (revokeAction.grant)
                    query = string.Format(delete_REVOKE, userId_grantee, revokeAction.table_name, 1, 1);
                else
                    query = string.Format(delete_REVOKE, userId_grantee, revokeAction.table_name, 1, 0);
            }
            //Execute the delete statement
            NonQuery(query);
        }
    }
}
