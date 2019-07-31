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
using System.Windows.Navigation;
using System.Windows.Shapes;

namespace CS505P2
{
    /// <summary>
    /// Interaction logic for MainMenu.xaml
    /// </summary>
    public partial class MainMenu : UserControl
    {
        public MainMenu()
        {
            InitializeComponent();
        }

        /// <summary>
        /// User clicks "Populate Table" button
        /// </summary>
        private void mm_button_populateTable_Click(object sender, RoutedEventArgs e)
        {
            string fileName = "";

            //Set up dialog box (to get input file)
            Microsoft.Win32.OpenFileDialog openFile = new Microsoft.Win32.OpenFileDialog();
            
            //Filter to text files
            openFile.DefaultExt = ".txt";
            openFile.Filter = "Text documents (.txt)|*.txt";

            //Show dialog box
            Nullable<bool> successfulFileDialogOpened = openFile.ShowDialog();

            //Get selected file
            if (successfulFileDialogOpened == true)
            {
                fileName = openFile.FileName;

                //Check that it's read
                if (!System.IO.File.Exists(fileName))
                    MessageBox.Show("Error: File does not exist!", "Bad File", MessageBoxButton.OK);
                //Create and populate the tablets
                else
                {
                    try
                    {
                        Service.StaticServices.PopulateTablets(fileName);
                        MessageBox.Show("Success: Data read!", "Success", MessageBoxButton.OK);
                    }
                    catch (Exception ex)
                    {
                        System.Diagnostics.Debug.WriteLine(ex.ToString());
                        MessageBox.Show(ex.Message, "Error (Exception)", MessageBoxButton.OK);
                    }
                }
                
            }      
        }

        /// <summary>
        /// User clicks "Query" button
        /// </summary>
        private void mm_button_query_Click(object sender, RoutedEventArgs e)
        {
            string transformedQuery = "";

            QueryInput qi = new QueryInput();
            qi.Focus();

            //Show Query input window
            if (qi.ShowDialog() == false)
            {
                //Get value of transformed query
                transformedQuery = qi.Transformed;
            }

            //if the transformed query is NOT empty (empty if they click exit rather than execute on that window)
            if (!string.IsNullOrEmpty(transformedQuery))
            {
                try
                {
                    string combinedResults = "";

                    //Execute:
                    List<List<string>> results = Service.StaticServices.ExecuteQuery(transformedQuery);
                    foreach (List<string> result in results)
                    {
                        //Format results
                        foreach (string field in result)
                            combinedResults += field + ";\t";
                        combinedResults += "\n";
                    }

                    //Display results to screen on Results window
                    ResultsWindow rw = new ResultsWindow(combinedResults);
                    rw.Focus();
                    if (rw.ShowDialog() == false)
                    {
                        this.Focus();
                    }
                }
                catch (Exception ex)
                {
                    System.Diagnostics.Debug.WriteLine(ex.ToString());
                    MessageBox.Show(ex.Message, "Error (Exception)", MessageBoxButton.OK);
                }
            }
        }
    }
}
