using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace CS505P2.Testing
{
    class Program
    {
        static void Main(string[] args)
        {
            Console.WriteLine("CS505 Project 2 Function Testing");

            int ct = 0;
            while (ct < 1)
            {
                //Show menu option
                int choice = ShowMenu();

                //Handle input
                try
                {
                    RouteToFunction(choice);
                    ct++;
                }
                catch (Exception ex)
                {
                    Console.WriteLine(ex.Message);
                }
            }
            //Press Enter to continue:
            Console.WriteLine("----- Press Return to Exit -----");
            Console.ReadLine();

        }

        private static int ShowMenu()
        {
            string s_choice;
            int i_choice;

            //Options
            Console.WriteLine("1. Parse a query");
            Console.WriteLine("2. Populate table from file");
            Console.WriteLine("3. Test database connection");
            Console.WriteLine("4. Execute scalar query");
            Console.WriteLine("5. Execute SQL query");

            //Read input
            Console.Write("input: ");
            s_choice = Console.ReadLine();

            //Parse
            if (!int.TryParse(s_choice, out i_choice))
                return -1;
            else
                return i_choice;
        }

        private static void RouteToFunction(int input)
        {
            string query;
            string table;

            switch (input)
            {
                case 1:
                    Console.Write("Query: ");
                    query = Console.ReadLine();

                    CS505P2.Service.StaticServices.ParseUserQuery(query);
                    break;
                case 2:
                    Console.Write("Table file: ");
                    table = Console.ReadLine();

                    CS505P2.Service.StaticServices.PopulateTablets(table, true);
                    break;
                case 3:
                    try
                    {
                        Service.StaticServices.ExecuteQuery("SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_TYPE = 'BASE TABLE'");
                        Console.WriteLine("Passed");
                    }
                    catch (Exception ex)
                    {
                        Console.WriteLine("Failed: {0}", ex.Message);
                    }
                    break;
                case 4:
                    Console.WriteLine("Query: ");
                    query = Console.ReadLine();
                    int result = Service.StaticServices.ExecuteScalar(query);

                    Console.WriteLine("Result: {0}", result);
                    break;
                case 5:
                    Console.WriteLine("Query: ");
                    query = Console.ReadLine();

                    List<List<string>> results = new List<List<string>>();
                    results = Service.StaticServices.ExecuteQuery(query);

                    Console.WriteLine("Results: ");
                    foreach (List<string> r in results)
                    {
                        foreach (string i in r)
                            Console.Write("{0};\t", i);
                        Console.WriteLine();
                    }
                    break;
            }
        }
    }
}
