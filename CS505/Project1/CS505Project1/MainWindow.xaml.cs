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

namespace CS505Project1
{
    /// <summary>
    /// Interaction logic for MainWindow.xaml
    /// </summary>
    public partial class MainWindow : Window
    {
        CS505Project1.Services.StandardService dbServices;
        private string _currentUser;

        public MainWindow()
        {
            InitializeComponent();
            _currentUser = null;
            dbServices = new Services.StandardService();
            this.l_combobox_user.ItemsSource = dbServices.GetAllUserNames();
            this.login_grid_main.Visibility = System.Windows.Visibility.Visible;
        }

        #region Main Menu Button Clicks

        
        ///Data Button
        private void main_options_displayControl_Click(object sender, RoutedEventArgs e)
        {
            this.main_grid_options.Visibility = Visibility.Hidden;
            this.TableDisplayer_Grid.Visibility = System.Windows.Visibility.Visible;
            //Show tables
            this.d_grid_tables.Visibility = System.Windows.Visibility.Visible;
            this.d_listView_tableList.ItemsSource = dbServices.GetAllowedTableNames();
        }

        ///Grant Button (formerly "User" button)
        private void main_options_userControl_Click(object sender, RoutedEventArgs e)
        {
            //Populate comboboxes
            this.g_combobox_user.ItemsSource = dbServices.GetAllUserNames();

            //Table dropdown list:
            this.g_combobox_table.ItemsSource = dbServices.GetAllowedTableNames();
            
            //Show forms
            this.main_grid_options.Visibility = Visibility.Hidden;
            this.Grant_Grid.Visibility = System.Windows.Visibility.Visible;
        }

        ///Admin Button
        private void main_options_adminControl_Click(object sender, RoutedEventArgs e)
        {
            //check that user is admin:
            if (_currentUser != "admin")
            {
                MessageBox.Show("Administrators only!");
                return;
            }
            this.main_grid_options.Visibility = Visibility.Hidden;
            this.Admin_Grid.Visibility = System.Windows.Visibility.Visible;
        }

        ///Permitted List Button
        private void main_options_permittedControl_Click(object sender, RoutedEventArgs e)
        {
            this.main_grid_options.Visibility = Visibility.Hidden;
            this.Permitted_Grid.Visibility = System.Windows.Visibility.Visible;

            //Get permitted table records
            List<string> permittedTable = dbServices.GetRecords(Services.StandardService.query_GETPERMITTEDLIST, '\t');

            this.p_listView_table.ItemsSource = permittedTable;
        }

        ///Forbidden List Button
        private void main_options_forbiddenControl_Click(object sender, RoutedEventArgs e)
        {
            this.main_grid_options.Visibility = Visibility.Hidden;
            this.Forbidden_Grid.Visibility = System.Windows.Visibility.Visible;
            
            //Get forbidden table records
            List<string> forbiddenTable = dbServices.GetRecords(Services.StandardService.query_GETFORBIDDENLIST, '\t');

            this.f_listView_table.ItemsSource = forbiddenTable;
        }
        #endregion

        //Home button
        private void main_button_home_Click(object sender, RoutedEventArgs e)
        {
            HideAll();

            this.main_grid_options.Visibility = System.Windows.Visibility.Visible;
        }

        #region DATA CONTROLS

        ///User selects a table to view
        ///Validate that the user is permitted to READ tables
        ///If permitted display the table
        private void d_listView_tableList_SelectionChanged(object sender, SelectionChangedEventArgs e)
        {
            //Get table name
            string tableName = this.d_listView_tableList.SelectedValue.ToString();

            //Format the action the user is trying to perform
            CS505Project1.Domains.TableAction tableAction = new Domains.TableAction();
            tableAction.user_name = _currentUser;
            tableAction.table_name = tableName;
            tableAction.operation = Domains.Operation_Type.SELECT;

            //Attempt to display the table
            try
            {
                dbServices.CanPerformTableAction(tableAction);
            }
            catch (Exception ex)
            {
                //User was NOT permitted (or was forbidden) to view that table!
                //Display to the user:
                MessageBox.Show(ex.Message.ToString());
                return;
            }

            //Show table
            this.TableDisplayer_Grid.Visibility = System.Windows.Visibility.Visible;
            this.d_grid_records.Visibility = System.Windows.Visibility.Visible;
            

            //Hide Table chooser
            this.d_grid_tables.Visibility = System.Windows.Visibility.Hidden;

            //Todo: Populate Table Displayer with table data
            this.d_listView_recordList.ItemsSource = dbServices.GetTable(tableName);
        }

        //TODO: DELETE
        private void d_button_newTable_Click(object sender, RoutedEventArgs e)
        {

        }

        //TODO: DELETE
        private void d_button_dropTable_Click(object sender, RoutedEventArgs e)
        {

        }

        private void d_record_button_newRecord_Click(object sender, RoutedEventArgs e)
        {
            if (this.d_listView_tableList.SelectedValue == null)
                return;

            //hide old panel
            this.d_grid_records.Visibility = System.Windows.Visibility.Hidden;
            //Prompt record values to add
            this.d_newrecord_grid.Visibility = System.Windows.Visibility.Visible;
                
            //Acquire the name of the selected table
            string tableName = this.d_listView_tableList.SelectedValue.ToString();
            //Set title labe
            this.d_newrecord_label_tableName.Text = tableName;
            
            //Get column names
            List<string> columns = dbServices.GetColumnNames(tableName);

            this.d_newrecord_label_column1.Text = columns[1];
            this.d_newrecord_label_column2.Text = columns[2];
            this.d_newrecord_label_column3.Text = columns[3];
            
            //Todo: Refresh list
        }

        //TODO: ONLY FOR ADMIN
        private void d_record_button_deleteRecord_Click(object sender, RoutedEventArgs e)
        {
            //Todo: Delete record from table being viewed
            //Todo: Refresh list
        }

        ///UI Specific
        ///Return to "Table" view page from the "Records" view page
        ///(Show list of tables instead of records in the previously chosen table)
        private void d_record_back_Click(object sender, RoutedEventArgs e)
        {
            this.TableDisplayer_Grid.Visibility = System.Windows.Visibility.Visible;
            this.d_grid_records.Visibility = System.Windows.Visibility.Hidden;
            this.d_grid_tables.Visibility = System.Windows.Visibility.Visible;
        }
        #endregion

        #region FORBIDDEN CONTROLS
        private void f_button_removeUser_Click(object sender, RoutedEventArgs e)
        {
            //Get selected entry
            if(this.f_listView_table.SelectedItem == null)
                return;
            string selection = this.f_listView_table.SelectedItem.ToString();

            string[] fields = selection.Split('\t');

            //remove it from forbidden list
            if (fields.Length != 4)
                return;
            string s_userId = fields[0];
            string s_table_name = fields[1];
            string s_operation = fields[2];
            string s_grant = fields[3];

            int i_userId = 0;
            bool b_operation = false, b_grant = false;
            if(!int.TryParse(s_userId, out i_userId))
                return;
            if (!bool.TryParse(s_operation, out b_operation))
                return;
            if (!bool.TryParse(s_grant, out b_grant))
                return;

            try
            {
                dbServices.RemoveFromPermissionOrForbidden(false, i_userId, s_table_name, b_operation, b_grant);
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }
        }
        #endregion

        #region PERMITTED CONTROLS
        private void p_button_removeUser_Click(object sender, RoutedEventArgs e)
        {
            //Get selected entry
            if (this.p_listView_table.SelectedItem == null)
                return;
            string selection = this.p_listView_table.SelectedItem.ToString();

            string[] fields = selection.Split('\t');

            //remove it from forbidden list
            if (fields.Length < 4)
                return;
            string s_userId = fields[0];
            string s_table_name = fields[1];
            string s_operation = fields[2];
            string s_grant = fields[3];

            int i_userId = 0;
            bool b_operation = false, b_grant = false;
            if (!int.TryParse(s_userId, out i_userId))
                return;
            if (!bool.TryParse(s_operation, out b_operation))
                return;
            if (!bool.TryParse(s_grant, out b_grant))
                return;

            try
            {
                dbServices.RemoveFromPermissionOrForbidden(true, i_userId, s_table_name, b_operation, b_grant);
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }
        }
        #endregion

        #region ADMIN CONTROLS
        ///"Submit Button" for new user
        ///Read in all data provided by the user,
        ///Validate said data.  Populate user table with data.
        private void nu_button_saveUser_Click(object sender, RoutedEventArgs e)
        {
            //Validate all textboxes:
            string user_name = this.nu_textbox_userName.Text;
            string first_name = this.nu_textbox_firstName.Text;
            string last_name = this.nu_textbox_lastName.Text;

            string[] textFields = { user_name, first_name, last_name };
            foreach (string field in textFields)
            {
                //Check that none of the fields are empty
                if (string.IsNullOrEmpty(field))
                {
                    MessageBox.Show("Error: Empty text field detected!  Please provide all inputs.");
                    return;
                }
            }

            try
            {
                dbServices.CreateUser(user_name, first_name, last_name);
            }
            catch (Exception ex)
            {
                MessageBox.Show(string.Format("Error: Could not create new user.\nException:", ex.Message.ToString()));
            }

            //Reset forms
            this.nu_textbox_userName.Text = string.Empty;
            this.nu_textbox_firstName.Text = string.Empty;
            this.nu_textbox_lastName.Text = string.Empty;

            //Back to admin page
            this.Admin_Grid.Visibility = Visibility.Visible;
            this.New_User_Grid.Visibility = System.Windows.Visibility.Hidden;
        }

        ///UI specific
        ///ACTION: Admin menu: CLICK TO CREATE NEW USER
        private void a_button_newUser_Click(object sender, RoutedEventArgs e)
        {
            this.Admin_Grid.Visibility = Visibility.Hidden;
            this.New_User_Grid.Visibility = System.Windows.Visibility.Visible;
        }

        ///UI specific
        ///Admin menu: CLICK TO CREATE ENTRY TO PERMITTED OR FORBIDDEN LIST
        private void a_button_permission_Click(object sender, RoutedEventArgs e)
        {
            //populate drop downs for New Permitted/Forbidden entry control
            //User dropdown list:
            this.np_combobox_user.ItemsSource = dbServices.GetAllUserNames();

            //Table dropdown list:
            this.np_combobox_table.ItemsSource = dbServices.GetAllowedTableNames();

            //hide old controls
            this.Admin_Grid.Visibility = System.Windows.Visibility.Hidden;
            //show permission control
            this.New_Permission_Grid.Visibility = System.Windows.Visibility.Visible;
        }

        //INCORRECT
        private void np_button_submit_Click(object sender, RoutedEventArgs e)
        {
            //Get data from fields:
            string s_permission = this.np_combobox_permission.SelectionBoxItem.ToString();
            string user_name = this.np_combobox_user.SelectedValue.ToString();
            string table_name = this.np_combobox_table.SelectedValue.ToString();
            string operation = this.np_combobox_operation.SelectedValue.ToString();
            string grant = this.np_combobox_grant.SelectedValue.ToString();

            #region Permission object Assignment
            CS505Project1.Domains.Permission permission = new Domains.Permission();
            permission.user_name = user_name;
            permission.table_name = table_name;

            if (s_permission == "Forbid")
                permission.type = Domains.Permission_Type.FORBIDDEN;
            else
                permission.type = Domains.Permission_Type.PERMITTED;

            if (operation == "SELECT")
                permission.write = false;
            else
                permission.write = true;

            if (grant == "With")
                permission.grant = true;
            else
                permission.grant = false;
            #endregion

            CS505Project1.Domains.GrantAction grantAction = new Domains.GrantAction();
            grantAction.grantor_name = _currentUser;
            grantAction.grantee_name = user_name;
            if (operation == "SELECT")
                grantAction.operation = CS505Project1.Domains.Operation_Type.SELECT;
            else
                grantAction.operation = CS505Project1.Domains.Operation_Type.INSERT;
            if (grant == "With")
                grantAction.grant = true;
            else
                grantAction.grant = false;
            

            try
            {
                dbServices.AddPermissOrForbid(permission);
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message.ToString());
            }
            finally
            {
                //hide old controls
                this.Admin_Grid.Visibility = System.Windows.Visibility.Visible;
                //show permission control
                this.New_Permission_Grid.Visibility = System.Windows.Visibility.Hidden;
            }
        }

        ///UI specific
        ///Change the "Submit" button to either "Permit!" or "Forbid!" depending on their choice of action.
        private void np_combobox_permission_SelectionChanged(object sender, SelectionChangedEventArgs e)
        {
            //Change the submit button text
            if (this.np_button_submit != null)
                this.np_button_submit.Content = this.np_combobox_permission.SelectedValue.ToString().Substring(this.np_combobox_permission.SelectedValue.ToString().LastIndexOf(':') + 2) + "!";
        }

        #endregion

        ///ACTION: User clicks login button (Set current user, change screen to display home menu
        private void l_button_login_Click(object sender, RoutedEventArgs e)
        {
            //set currentUser
            _currentUser = l_combobox_user.SelectedItem.ToString().Trim();

            //hide log in page
            this.login_grid_main.Visibility = System.Windows.Visibility.Hidden;

            //show main menu
            this.main_grid_options.Visibility = System.Windows.Visibility.Visible;

            //Enable home button
            this.main_button_home.IsEnabled = true;
            //Enable log out button
            this.main_button_logout.IsEnabled = true;
        }


        private void d_newrecord_button_submit_Click(object sender, RoutedEventArgs e)
        {
            //TRY TO INSERT NEW RECORD
            //Create TableAction object
            CS505Project1.Domains.TableAction tableAction = new Domains.TableAction();
            tableAction.user_name = _currentUser;
            tableAction.table_name = this.d_listView_tableList.SelectedValue.ToString().Trim();
            tableAction.operation = Domains.Operation_Type.INSERT;

            //Send tableAction to find out if this action conflicts
            try
            {
                dbServices.CanPerformTableAction(tableAction);

            }
            catch (Exception ex)
            {
                //Not allowed to perform this action!
                MessageBox.Show(ex.Message.ToString());
                return;
            }

            try
            {
                string value1 = this.d_newrecord_textbox1.Text;
                string value2 = this.d_newrecord_textbox2.Text;
                string value3 = this.d_newrecord_textbox3.Text;

                dbServices.PerformGenericInsert(tableAction.table_name, value1, value2, value3);
            }
            catch (Exception ex)
            {
                //Error occured on insert
                MessageBox.Show(ex.Message.ToString());
                return;
            }
        }

        private void main_button_logout_Click(object sender, RoutedEventArgs e)
        {
            HideAll();
            _currentUser = null;
            this.login_grid_main.Visibility = System.Windows.Visibility.Visible;

            //Enable home button
            this.main_button_home.IsEnabled = false;
            //Enable log out button
            this.main_button_logout.IsEnabled = false;
        }

        private void HideAll()
        {
            //Hide other views
            this.Forbidden_Grid.Visibility = System.Windows.Visibility.Hidden;
            this.Permitted_Grid.Visibility = System.Windows.Visibility.Hidden;
            this.Admin_Grid.Visibility = System.Windows.Visibility.Hidden;
            this.Grant_Grid.Visibility = System.Windows.Visibility.Hidden;
            this.TableDisplayer_Grid.Visibility = System.Windows.Visibility.Hidden;
            this.d_grid_records.Visibility = System.Windows.Visibility.Hidden;
            this.d_grid_tables.Visibility = System.Windows.Visibility.Hidden;
            this.New_Permission_Grid.Visibility = System.Windows.Visibility.Hidden;
            this.New_User_Grid.Visibility = System.Windows.Visibility.Hidden;
            this.d_newrecord_grid.Visibility = System.Windows.Visibility.Hidden;
            this.main_grid_options.Visibility = System.Windows.Visibility.Hidden;

            this.d_record_button_deleteRecord.Visibility = System.Windows.Visibility.Visible;
            this.d_record_button_newRecord.Visibility = System.Windows.Visibility.Visible;
        }

        private void main_options_myPermissions_Click(object sender, RoutedEventArgs e)
        {
            HideAll();

            //Show table
            this.TableDisplayer_Grid.Visibility = System.Windows.Visibility.Visible;
            this.d_grid_records.Visibility = System.Windows.Visibility.Visible;
            //hide new/drop buttons, users don't edit this
            this.d_record_button_deleteRecord.Visibility = System.Windows.Visibility.Hidden;
            this.d_record_button_newRecord.Visibility = System.Windows.Visibility.Hidden;


            //Hide Table chooser
            this.d_grid_tables.Visibility = System.Windows.Visibility.Hidden;

            //Todo: Populate Table Displayer with table data
            this.d_listView_recordList.ItemsSource = dbServices.MyPermissions(_currentUser);
        }

        private void g_combobox_action_SelectionChanged(object sender, SelectionChangedEventArgs e)
        {
            //Change the submit button text
            if (this.g_button_submit != null)
                this.g_button_submit.Content = this.g_combobox_action.SelectedValue.ToString().Substring(this.g_combobox_action.SelectedValue.ToString().LastIndexOf(':') + 2) + "!";
        }

        private void g_button_submit_Click(object sender, RoutedEventArgs e)
        {
            string action = this.g_combobox_action.SelectionBoxItem.ToString();

            //Grant or revoke:
            CS505Project1.Domains.GrantAction grantAction = new Domains.GrantAction();
            grantAction.grantor_name = _currentUser;
            grantAction.grantee_name = this.g_combobox_user.SelectedItem.ToString();
            grantAction.table_name = this.g_combobox_table.SelectedItem.ToString();
            string operation = (this.g_combobox_operation.SelectedValue as ComboBoxItem).Content.ToString();
            string grant = (this.g_combobox_grant.SelectedValue as ComboBoxItem).Content.ToString();

            if (operation == "SELECT")
                grantAction.operation = Domains.Operation_Type.SELECT;
            else
                grantAction.operation = Domains.Operation_Type.INSERT;

            if (grant == "With")
                grantAction.grant = true;
            else
                grantAction.grant = false;

            try
            {
                if (action == "Revoke")
                {
                    //Try Revoke
                    dbServices.Revoke(grantAction);
                }
                else
                {
                    //Try Grant
                    dbServices.Grant(grantAction);
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
                return;
            }

            MessageBox.Show("Action successful!");
        }
    }
}
