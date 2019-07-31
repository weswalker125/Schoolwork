using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;

using CS505Project1.Domains;
using CS505Project1.Services;

namespace CS505Project1.Console
{
    class Program
    {
        static void Main(string[] args)
        {
            System.Console.WriteLine("1. Grant");
            System.Console.WriteLine("2. Revoke");

            System.Console.Write("\nChoice: ");
            string s_choice = System.Console.ReadLine();
            int i_choice;

            if (!int.TryParse(s_choice, out i_choice))
                return;

            switch (i_choice)
            {
                case 1:
                    Grant();
                    break;
                case 2:
                    Revoke();
                    break;
            }

            return;
        }

        public static void Grant()
        {
            CS505Project1.Services.StandardService dbServices = new StandardService();

            //Mom tries to give <dad, purchases, select, without>
            GrantAction grant =  new GrantAction();
            grant.grantor_name = "mom";
            grant.grantee_name = "PhilCollins";
            grant.table_name = "clients";
            grant.operation = Operation_Type.SELECT;
            grant.grant = false;

            dbServices.Grant(grant);

        }

        public static void Revoke()
        {
            CS505Project1.Services.StandardService dbServices = new StandardService();

            //Mom tries to give <dad, purchases, select, without>
            GrantAction grant = new GrantAction();
            grant.grantor_name = "mom";
            grant.grantee_name = "PhilCollins";
            grant.table_name = "clients";
            grant.operation = Operation_Type.SELECT;
            grant.grant = false;

            dbServices.Revoke(grant);
        }
    }
}
