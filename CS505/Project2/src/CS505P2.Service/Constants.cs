using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace CS505P2.Service
{
    /// <summary>
    /// Object containing static values which are repeated referenced and remain unchanged throughout execution.
    /// </summary>
    public static class Constants
    {
        public const int MAX_COLUMNS = 12;
        public const string DB_CONNECTION_STRING = "Server=BANE\\DATAXCORE;Database=CS505DB;User Id=sa;Password=lookslikeyou12;";
        public static string[] operators = { "NOT LIKE", "LIKE", "=", "<>", "<", ">" };
        public static string[] junctions = { "AND NOT", "OR NOT", "AND", "OR" };
        public static string[] varTablet = { "t0", "t1", "t2", "t3", "t4", "t5", "t6", "t7", "t8", "t9", "t10"};

        public static string INSERT_RECORD(string baseTable, string columnName, int serialKey, string value)
        {
            return string.Format("INSERT INTO {0} VALUES('{1}', '{2}')", baseTable + "_" + columnName, serialKey, value);
        }

        public static string CREATE_TABLET(string baseTable, string columnName, string dataType)
        {
            return string.Format("CREATE TABLE {0}_{1} ( serial bigint, {1} {2} )", baseTable, columnName, dataType);
        }
    }

    public struct TableInfo
    {
        public string baseTableName;
        public string[] columnNames;
        public string[] columnTypes;
    }

    public class QueryInfo
    {
        public List<string> Select;
        public string From;
        public List<Condition> Where;
        public List<string> WhereJunctions;

        public QueryInfo()
        {
            Select = new List<string>();
            From = "";
            Where = new List<Condition>();
            WhereJunctions = new List<string>();
        }
    }

    public struct TransformedQueryInfo
    {
        public string Select;
        public string From;
        public string Where;
    }

    public struct Condition
    {
        public string LHS;
        public string Operator;
        public string RHS;
    }
}
