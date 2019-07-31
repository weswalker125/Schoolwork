using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Shapes;

namespace CS505P2
{
    /// <summary>
    /// Interaction logic for QueryInput.xaml
    /// </summary>
    public partial class QueryInput : Window
    {
        //String representing the transformed query
        public string Transformed { get; set; }

        public QueryInput()
        {
            InitializeComponent();
        }

        /// <summary>
        /// Execute the transformed query (closes this window, execution occurs on close)
        /// </summary>
        private void qi_button_executeQuery_Click(object sender, RoutedEventArgs e)
        {
            //Set variable of transformed query
            Transformed = this.qi_textblock_transformedQuery.Text;
            
            //Close (execution of query occurs on close from main menu code)
            this.Close();
        }

        /// <summary>
        /// Take the given SQL query, parse the string and generate the "transformed" query, which relates to the partitioning scheme.
        /// </summary>
        private void qi_button_transformQuery_Click(object sender, RoutedEventArgs e)
        {
            //Get text from textbox, parse, display transformed query
            string input = this.qi_textBox_input.Text;
            try
            {
                string tranformed = CS505P2.Service.StaticServices.ParseUserQuery(input);
                qi_textblock_transformedQuery.Text = tranformed;

                //Enable execution button
                this.qi_button_executeQuery.IsEnabled = true;
            }
            catch (Exception ex)
            {
                System.Diagnostics.Debug.WriteLine(ex.ToString());
                MessageBox.Show(ex.Message, "Error (Exception)", MessageBoxButton.OK);
            }            
        }
    }
}
