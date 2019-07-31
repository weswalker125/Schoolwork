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
    /// Interaction logic for ResultsWindow.xaml
    /// </summary>
    public partial class ResultsWindow : Window
    {
        /// <summary>
        /// Display results
        /// </summary>
        /// <param name="results">string representation of query results</param>
        public ResultsWindow(string results)
        {
            InitializeComponent();
            this.rc_textBlock_results.Text = results;
        }

        /// <summary>
        /// Close this window
        /// </summary>
        private void rc_button_finish_Click(object sender, RoutedEventArgs e)
        {
            this.Close();
        }
    }
}
