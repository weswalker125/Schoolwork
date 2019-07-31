using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace CS505Project1.Domains
{
    public struct Permission
    {
        public Permission_Type type { get; set; }
        public string user_name { get; set; }
        public string table_name { get; set; }
        public bool write { get; set; }
        public bool grant { get; set; }
    }

    public struct TableAction
    {
        public Operation_Type operation { get; set; }
        public string user_name { get; set; }
        public string table_name { get; set; }
    }

    public struct GrantAction
    {
        public string grantor_name { get; set; }
        public string grantee_name { get; set; }
        public string table_name { get; set; }
        public Operation_Type operation { get; set; }
        public bool grant { get; set; }
    }


    public enum Permission_Type
    {
        PERMITTED,
        FORBIDDEN
    }

    public enum Operation_Type
    {
        SELECT,
        INSERT
    }
}
