import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.*;
import javax.imageio.*;
import java.io.File;
import java.net.URL;
import java.io.*;
import javax.swing.border.*;
import java.util.concurrent.TimeUnit;

/**
 * Write a description of class Interface here.
 *
 * @author William Wang
 * @version (a version number or a date)
 */
public class Interface implements ActionListener
{
    // constants
    final String[] HEADERS = {"Website: ","Username: ","Password: "};
    final String FRAME_NAME = "Your Very Own Offline Password Manager";
    final Color BACKGROUND_COLOR = Color.cyan;
    final Color OPTION_BACKGROUND = Color.green;
    final Color ACCOUNT_BACKGROUND = Color.yellow;

    // instance variables - replace the example below with your own
    JFrame frame;

    User user;

    JPanel accounts;
    JPanel account;
    JLabel website;
    JLabel username;
    JLabel password;
    BufferedImage img;

    JPanel option;
    JLabel optionLabel;
    JButton addAccount;
    JButton add;
    JButton editAccount;
    JButton editMenuButton;
    JButton refreshButton;
    JButton changeName;
    JButton changePassword;
    JButton undoButton;
    JButton quitProgram;

    JTextField websiteTextField, usernameTextField, passwordTextField;
    JButton save;
    JFrame addAccountFrame;

    JFrame editAccountFrame;
    JFrame editFrame;
    JPanel editPanel;
    JButton editButton;
    JPanel editOptionPanel;
    JButton backButton;
    JButton undo;
    JButton lock;

    JButton saveLast;
    JButton openLast;

    JButton  saveAs, openAs;
    String fileName = "Files\\sample.txt";
    String defaultPath = System.getProperty("user.dir") + File.separator + fileName;
    String lastPath = null;

    /**
     * Constructor for objects of class Display
     */
    public Interface()
    {
        user = new User();
        accountButton();
        displayJFrame();
    }

    /**
     * Constructor for objects of class Display
     */
    public Interface(User user)
    {
        this.user = user;
        accountButton();
        displayJFrame();
    }

    /**
     * Constructor for objects of class Display
     */
    public Interface(String name)
    {
        user = new User(name);
        accountButton();
        displayJFrame();
    }

    public JFrame getFrame()
    {
        return frame;
    }

    public void addOptionPanel()
    {
        // get option panel
        option = new JPanel();
        option.setLayout(new BorderLayout());

        JPanel right = new JPanel(new FlowLayout());
        JPanel left = new JPanel(new FlowLayout());

        JMenuBar fileBar = new JMenuBar();
        JMenu menu = new JMenu("Files");
        fileBar.add(menu);

        JMenuBar acc = new JMenuBar();
        JMenu accounts = new JMenu("Accounts");
        fileBar.add(accounts);

        JMenuBar userBar = new JMenuBar();
        JMenu userMenu = new JMenu("User");
        fileBar.add(userMenu);

        //add menu bar to jpanel
        left.add(fileBar);

        changeName = new JButton("Change Name");
        changeName.addActionListener(this);
        userMenu.add(changeName);

        changePassword = new JButton("Change Password");
        changePassword.addActionListener(this);
        userMenu.add(changePassword);

        lock = new JButton("Lock");
        lock.addActionListener(this);
        userMenu.add(lock);

        saveAs = new JButton("Save As"); 
        saveAs.addActionListener(this);
        menu.add(saveAs);

        openAs = new JButton("Open As");
        openAs.addActionListener(this);
        menu.add(openAs);

        saveLast = new JButton("Save Last");
        saveLast.addActionListener(this);
        menu.add(saveLast);

        openLast = new JButton("Open Last");
        openLast.addActionListener(this);
        menu.add(openLast);

        optionLabel = new JLabel();
        // add option label to option panel
        option.add(optionLabel, BorderLayout.CENTER);
        // set option panel background color
        option.setBackground(OPTION_BACKGROUND);
        refreshOptionLabel();

        // get add account button
        addAccount = new JButton("+");
        add = new JButton("Add Account");
        addAccount.addActionListener(this);
        add.addActionListener(this);
        addAccount.addActionListener(this);
        accounts.add(add);

        // get edit account button
        editAccount = new JButton("Edit Accounts");
        editMenuButton = new JButton("Edit");
        editAccount.addActionListener(this);
        editMenuButton.addActionListener(this);
        accounts.add(editAccount);

        undoButton = new JButton("Undo Button");
        undoButton.addActionListener(this);
        accounts.add(undoButton);

        // get refresh button
        refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(this);

        quitProgram = new JButton("Quit");
        quitProgram.addActionListener(this);

        right.add(quitProgram);
        right.add(refreshButton);
        right.add(editMenuButton);
        right.add(addAccount);

        option.add(left,BorderLayout.WEST);
        option.add(right, BorderLayout.EAST);

        left.setBackground(OPTION_BACKGROUND);
        right.setBackground(OPTION_BACKGROUND);

        frame.add(option, BorderLayout.NORTH);
    }

    public void recordAccounts(String path)
    {
        String absolutePath = path;
        String print = "";
        // write the content in file 
        try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(absolutePath)))
        {
            for(int i = 0; i < user.getNumberOfAccounts(); i++)
            {
                print = user.getAccount(i).toPrint() + "\n\n";
                System.out.println(print);
                bufferedWriter.write(user.getAccount(i).toPrint());
                bufferedWriter.write("\n\n");
            }
            bufferedWriter.close();
        } 
        catch (IOException e) 
        {
            // exception handling
            System.out.println("Input Output Error");
        }

    }

    public void openAccounts(String path)
    {
        String absolutePath = path;
        String input = "";
        // try read the content from file
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(absolutePath))) 
        {
            String line = bufferedReader.readLine();
            String website = "";
            String username = "";
            String password = "";
            while(line != null) {
                if(line.contains("Website: "))
                {
                    String[] l = line.split("Website: ");
                    website = l[1];
                    line = bufferedReader.readLine();
                    l = line.split("Username: ");
                    username = l[1];
                    line = bufferedReader.readLine();
                    l = line.split("Password: ");
                    password = l[1];
                    user.addAccount(website, username, password);
                }
                else
                {
                    line = bufferedReader.readLine();
                }
            }
            bufferedReader.close();
        } 
        catch (FileNotFoundException e) {
            // exception handling
        } catch (IOException e) {
            // exception handling
        }
        refresh();
    }

    public void accountButton()
    {
        // initialise instance variables
        addAccountFrame = new JFrame();
        websiteTextField = new JTextField("website");
        JLabel websiteLabel = new JLabel("Website:");
        websiteTextField.addActionListener(this);
        usernameTextField = new JTextField("username");
        JLabel usernameLabel = new JLabel("Username:");
        usernameTextField.addActionListener(this);
        passwordTextField = new JTextField("password");
        JLabel passwordLabel = new JLabel("Password:");
        passwordTextField.addActionListener(this);

        JPanel panel = new JPanel();
        save = new JButton("Save");
        save.addActionListener(this);

        panel.setLayout(new GridLayout(7,1));
        panel.setPreferredSize(new Dimension(300,400));
        panel.add(websiteLabel);
        panel.add(websiteTextField);
        panel.add(usernameLabel);
        panel.add(usernameTextField);
        panel.add(passwordLabel);
        panel.add(passwordTextField);
        panel.add(save);
        addAccountFrame.add(panel);
        pack(addAccountFrame);
        addAccountFrame.setVisible(false);
    }

    /**
     * Display the JFrame
     *
     * @param  y  a sample parameter for a method
     * @return    the sum of x and y
     */
    public void displayJFrame() 
    {
        // put your code here
        frame = new JFrame(FRAME_NAME);
        // optionPanel.addOptionPanel();
        addOptionPanel();
        // display user accounts
        displayUser(user);

        packMainFrame();
    }

    public void displayUser(User user)
    {
        accounts = new JPanel();
        displayAccounts();
    }

    public void refreshOptionLabel()
    {
        String n = user.getName();
        if(n.equals("unnamed user"))
        {
            n = n + "; Please click the change name item to input a name. :)";
        }
        else
        {

        }
        optionLabel.setText("Name: " + n + " | Number Of Accounts: " + user.getNumberOfAccounts());
    }

    public void refresh()
    {
        // get option label
        refreshOptionLabel();
        packMainFrame();
    }

    public void addAccount(String website, String username, String password)
    {
        user.addAccount(website, username, password);
    }

    public JPanel editAccountPanel(Account acc)
    {
        JPanel button = new JPanel();
        JButton edit = new JButton("edit");
        JButton deleteButton = new JButton("delete");

        edit.addActionListener(this);
        deleteButton.addActionListener(this);
        button.setLayout(new FlowLayout());
        // display image

        JLabel i = new JLabel();
        i.setIcon(acc.getIcon());
        button.add(i);

        // display the rest of the user info

        button.add(getLabels(acc)[0]);
        button.add(getLabels(acc)[1]);
        button.add(getLabels(acc)[2]);
        JPanel b = new JPanel();

        b.add(edit);
        b.add(deleteButton);
        button.add(b);
        deleteButton.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e) 
                {
                    if(e.getSource() == deleteButton)
                    {
                        user.removeAccount(user.getAccounts().indexOf(acc));
                        checkFrames();
                        refresh();
                    }
                }
            });
        edit.addActionListener(new ActionListener() 
            {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(e.getSource() == edit)
                    {
                        checkFrames();

                        editAccountFrame = new JFrame();
                        JTextField websiteTextField = new JTextField(acc.getWebsite());
                        JLabel websiteLabel = new JLabel("Website:");
                        websiteTextField.addActionListener(this);
                        JTextField usernameTextField = new JTextField(acc.getUsername());
                        JLabel usernameLabel = new JLabel("Username:");
                        usernameTextField.addActionListener(this);
                        JTextField passwordTextField = new JTextField(acc.getPassword());
                        JLabel passwordLabel = new JLabel("Password:");
                        passwordTextField.addActionListener(this);

                        editPanel = new JPanel();
                        editButton = new JButton("Save");
                        editButton.addActionListener(this);

                        editPanel.setLayout(new GridLayout(7,1));
                        editPanel.setPreferredSize(new Dimension(300,400));
                        editPanel.add(websiteLabel);
                        editPanel.add(websiteTextField);
                        editPanel.add(usernameLabel);
                        editPanel.add(usernameTextField);
                        editPanel.add(passwordLabel);
                        editPanel.add(passwordTextField);
                        editPanel.add(editButton);
                        editAccountFrame.add(editPanel);

                        // editAccountFrame.add(save);

                        pack(editAccountFrame);

                        editPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "Enter");
                        Action action = new AbstractAction()
                            {
                                @Override
                                public void actionPerformed(ActionEvent e)
                                {
                                    if(e.getSource() == editButton || e.getSource() == editPanel )
                                    {
                                        acc.editAccount(websiteTextField.getText(), usernameTextField.getText(), passwordTextField.getText());
                                        editAccountFrame.dispose();
                                        editFrame.dispose();
                                        refresh();
                                    }

                                }
                            };          
                        editPanel.getActionMap().put("Enter", action);
                        editButton.addActionListener(action);
                    }
                }
            });

        button.setPreferredSize(new Dimension(getWidth(acc), 180));
        button.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
        return button;
    }

    public int getHeight(JPanel panel)
    {
        return (int) panel.getPreferredSize().getHeight();
    }

    public JPanel getAccountPanel(Account acc)
    {
        account = new JPanel();
        account.setLayout(new FlowLayout());
        JLabel image = new JLabel(acc.getIcon(), JLabel.CENTER);
        JLabel[] array = new JLabel[3];
        JPanel text = new JPanel();

        account.setPreferredSize(new Dimension(150, 150));

        image.setBackground(ACCOUNT_BACKGROUND);
        account.add(image);

        for(int i = 0; i < array.length; i++)
        {
            // display in order webite, username and password
            array[i] = new JLabel(HEADERS[i] + acc.getAcc(i));
            int width = (int)array[i].getPreferredSize().getWidth();
            if(width > 150)
            {
                account.setPreferredSize(new Dimension(width + 2, 150));
                image.setPreferredSize(new Dimension(width, 75));
            }
            account.add(array[i]);
        }
        account.setBackground(ACCOUNT_BACKGROUND);
        return account;
    }

    public JLabel[] getLabels(Account acc)
    {
        JLabel image = new JLabel(acc.getIcon(), JLabel.CENTER);
        JLabel[] array = new JLabel[3];
        for(int i = 0; i < array.length; i++)
        {
            // display in order webite, username and password
            array[i] = new JLabel(HEADERS[i] + acc.getAcc(i));
            int width = (int)array[i].getPreferredSize().getWidth();
            if(width > 150)
            {
                account.setPreferredSize(new Dimension(width + 2, 150));
                image.setPreferredSize(new Dimension(width, 75));
            }
            account.add(array[i]);
        }
        return array;
    }

    public int getWidth(Account acc)
    {
        JLabel[] array = new JLabel[3];
        int width = 150;
        for(int i = 0; i < array.length; i++)
        {
            // display in order webite, username and password
            array[i] = new JLabel(HEADERS[i] + acc.getAcc(i));
            int w =(int)array[i].getPreferredSize().getWidth();
            if(w > width)
            {
                width = w;
            }
        }
        return width;
    }

    public JPanel displayAccounts()
    {
        accounts = new JPanel();
        if(user.getNumberOfAccounts() == 0)
        {
            accounts = noAccounts();
        }
        else
        {
            accounts.setBackground(BACKGROUND_COLOR);
            for(int i = 0; i < user.getNumberOfAccounts(); i++)
            {
                accounts.add(getAccountPanel(user.getAccount(i)));
            }
        }
        return accounts;
    }

    public void packMainFrame()
    {
        refreshOptionLabel();

        frame.add(displayAccounts(), BorderLayout.CENTER);

        // set up the jframe, then display it
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        frame.setPreferredSize(new Dimension(1000, 800));

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public JPanel noAccounts()
    {
        JPanel empty = new JPanel();
        JLabel n = new JLabel("Welcome!");
        n.setPreferredSize(new Dimension(800,100));
        n.setHorizontalAlignment(JLabel.CENTER);
        JLabel a = new JLabel("You do not currently have any accounts stored on this user.");
        JLabel b = new JLabel("Click on the Open As menu option and select the sample.txt to add some sample accounts.");
        JLabel c = new JLabel("Click on the + or Add Account button to add your own accounts.");
        JLabel d = new JLabel("Please click the Refresh button if the changes do not show up");
        n.setFont(new Font("Serif", Font.PLAIN, 40));
        a.setFont(new Font("Serif", Font.PLAIN, 30));
        b.setFont(new Font("Serif", Font.PLAIN, 20));
        c.setFont(new Font("Serif", Font.PLAIN, 20));
        d.setFont(new Font("Serif", Font.PLAIN, 40));
        empty.add(n);
        empty.add(a);
        empty.add(b);
        empty.add(c);
        empty.add(d);
        empty.setBackground(BACKGROUND_COLOR);
        return empty;
    }

    public void pack(JFrame jframe)
    {
        jframe.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        jframe.pack();
        jframe.setLocationRelativeTo(null);
        jframe.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) 
    {
        //...Get information from the action event...
        //...Display it in the text area...

        if(e.getSource() == addAccount || e.getSource() == add)
        {
            addAccountFrame.setVisible(true);
        }
        else if(e.getSource() == saveLast)
        {
            if(lastPath != null)
            {
                recordAccounts(lastPath);
                JOptionPane.showMessageDialog(frame,
                    "Accounts Successfully Saved :))");
            }
            else
            {
                JOptionPane.showMessageDialog(frame,
                    "Operation Unsuccessful. No Previous File to Save. Please Try Again. :((");
            }
        }
        else if(e.getSource()==saveAs)
        {
            FileChooser f = new FileChooser();
            String path = f.getPath();
            if(path != "")
            {
                lastPath = path;
                recordAccounts(path);
                JOptionPane.showMessageDialog(frame,
                    "Accounts Successfully Saved :))");
            }
            else
            {
                JOptionPane.showMessageDialog(frame,
                    "Operation Unsuccessful. Please Try Again. :((");
            }
        }
        else if(e.getSource() == openLast)
        {
            if(lastPath != null)
            {
                openAccounts(lastPath);
                JOptionPane.showMessageDialog(frame,
                    "Accounts Successfully Added :))");
            }
            else
            {
                JOptionPane.showMessageDialog(frame,
                    "Operation Unsuccessful. No Previous File to Read. Please Try Again. :((");
            }
        }
        else if (e.getSource()==openAs)
        {
            FileChooser f = new FileChooser();
            String path = f.getPath();
            if(path != "")
            {
                lastPath = path;
                openAccounts(path);
                JOptionPane.showMessageDialog(frame,
                    "Accounts Successfully Added :))");
            }
            else
            {
                JOptionPane.showMessageDialog(frame,
                    "Operation Unsuccessful. Please Try Again. :((");
            }
        }
        else if(e.getSource() == save)
        {
            // save textfield info into account
            addAccount(websiteTextField.getText(), usernameTextField.getText(), passwordTextField.getText());
            addAccountFrame.setVisible(false);
            refresh();
        }
        else if(e.getSource() == refreshButton)
        {
            // refresh
            refresh();
        }
        else if(e.getSource() == backButton)
        {
            // save textfield info into account
            editFrame.dispose();
        }
        else if(e.getSource() == lock)
        {
            // save textfield info into account
            if(user.getPassword().equals(""))
            {
                JOptionPane.showMessageDialog(frame,
                    "Please add a password first before you lock.");
                UserSettings u = new UserSettings();
                u.changePassword();
                refresh();
            }
            else
            {
                frame.setVisible(false);
                Login l = new Login();
            }

        }
        else if(e.getSource() == undo || e.getSource() == undoButton)
        {
            user.undo();
            checkFrames();
            refresh();
        }
        else if(e.getSource() == changePassword)
        {
            if(user.getPassword().equals(""))
            {
                JOptionPane.showMessageDialog(frame,
                    "Please add a password first :))");
                UserSettings u = new UserSettings();
                u.changePassword();
                refresh();
            }
            else
            {
                JOptionPane.showMessageDialog(frame,
                    "Please confirm your password first :))");
                frame.setVisible(false);
                Login l = new Login();
                l.needPasswordChange();
            }
        }
        else if(e.getSource() == changeName)
        {
            UserSettings u = new UserSettings();
            u.changeName();
            refresh();
        }
        else if(e.getSource() == editAccount || e.getSource() == editMenuButton)
        {
            addEditFrame();
        }
        else if(e.getSource() == quitProgram )
        {
            quit();
        }
    }

    public void quit()
    {
        Object[] options = {"Yes, but I would like to save my passwords.",
                "Yes, without saving",
                "No"} ;
        int n = JOptionPane.showOptionDialog(frame,
                "Are You Sure?",
                "Quit Confirmation",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[2]);

        switch (n)
        {
            case 0:
            boolean s = false;
            do
            {
                s = false;
                FileChooser f = new FileChooser();
                String path = f.getPath();
                if(path != "")
                {
                    lastPath = path;
                    recordAccounts(path);
                    JOptionPane.showMessageDialog(frame,
                        "Accounts Successfully Saved :))");
                    s = true;
                }
                else
                {
                    JOptionPane.showMessageDialog(frame,
                        "Operation Unsuccessful. Please Try Again. :((");
                }
            }
            while(!s);
            case 1:
            JOptionPane.showMessageDialog(frame,
                "Goodbye :))");
            System.exit(0);
            break;
            case 2:
            break;
            default:
            break;
        }
    }

    public void addEditFrame()
    {
        JButton button;
        JPanel edits;

        checkFrames();

        edits = new JPanel(new FlowLayout());
        editFrame = new JFrame("edit account");

        editOptionPanel = new JPanel();

        backButton = new JButton("go back");
        backButton.addActionListener(this);

        undo = new JButton("undo");
        undo.addActionListener(this);

        editOptionPanel.add(backButton);
        editOptionPanel.add(undo);

        editFrame.add(editOptionPanel, BorderLayout.NORTH);
        editOptionPanel.setBackground(OPTION_BACKGROUND);
        for(int i = 0; i < user.getNumberOfAccounts(); i++)
        {
            edits.add(editAccountPanel(user.getAccount(i)));
        }
        editFrame.add(edits);
        editFrame.setPreferredSize(new Dimension(800, 900));
        pack(editFrame);
    }

    public void checkFrames()
    {
        if(editFrame != null)
        {                
            editFrame.dispose();                               
        }
        if(editAccountFrame != null)
        {
            editAccountFrame.dispose();
        }      
    }

    private class UserSettings implements ActionListener
    {
        JFrame frame;
        JTextField inputTextField;
        JLabel inputLabel;
        JPanel panel;
        JButton start;
        String in = "";
        String display = "";

        public void changeName()
        {
            needPasswordChange("Name");
        }

        public void changePassword()
        {
            needPasswordChange("Password");
        }

        public boolean needPasswordChange(String display)
        {
            frame = new JFrame(display);
            panel = new JPanel(new GridLayout(3,1));
            boolean done = false;
            this.display = display;

            inputTextField = new JTextField("");
            inputTextField.setFont(new Font("Serif", Font.PLAIN, 20));
            inputLabel = new JLabel("Please Enter Your " + display + " Below:");
            inputLabel.setFont(new Font("Serif", Font.PLAIN, 20));
            start = new JButton("Start!");
            start.addActionListener(this);

            panel.add(inputLabel);
            panel.add(inputTextField);
            panel.add(start);

            start.setPreferredSize(new Dimension(200, 100));

            frame.add(panel);
            frame.setPreferredSize(new Dimension(400, 300));
            frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
            frame.setResizable(false);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

            inputTextField.addActionListener(this);

            return done;
        }

        /**
         * An example of a method - replace this comment with your own
         *
         * @param  y  a sample parameter for a method
         * @return    the sum of x and y
         */
        public void actionPerformed(ActionEvent e) 
        {
            //...Get information from the action event...
            //...Display it in the text area...

            if(e.getSource() == inputTextField || e.getSource() == start)
            {
                String input = inputTextField.getText();
                if(display.equals("Name"))
                {
                    user.changeName(input);
                }
                else if(display.equals("Password"))
                {
                    user.changePassword(input);
                }
                frame.setVisible(false);
                refresh();
            }
        }
    }

    private class Login implements ActionListener
    {
        // instance variables - replace the example below with your own
        JFrame frame;
        JTextField nameTextField;
        JLabel nameLabel, img;
        JPanel panel;
        JButton start;
        Interface i;
        Color c = new Color(73, 63, 100);
        boolean change = false;

        /**
         * Constructor for objects of class Login
         */
        public Login()
        {
            // initialise instance variables
            frame = new JFrame("Login");
            frame.getContentPane().setLayout(new FlowLayout());
            panel = new JPanel(new GridLayout(3,1));
            img = new JLabel();
            ImageIcon icon = new ImageIcon(System.getProperty("user.dir") + File.separator + "Icons\\newUserLogin.png");

            Image image = icon.getImage(); // transform it
            Image newimg = image.getScaledInstance(700, 700,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
            icon = new ImageIcon(newimg);  // transform it back
            img.setIcon(icon);

            frame.add(img);
            frame.getContentPane().setBackground(c);

            nameTextField = new JTextField("");
            nameTextField.setFont(new Font("Serif", Font.PLAIN, 20));
            nameLabel = new JLabel("Enter Your Password Below:");
            nameLabel.setFont(new Font("Serif", Font.PLAIN, 20));
            start = new JButton("Start!");
            nameTextField.addActionListener(this);
            start.addActionListener(this);

            panel.add(nameLabel);
            panel.add(nameTextField);
            panel.add(start);

            start.setPreferredSize(new Dimension(200, 100));

            frame.add(panel);
            frame.setPreferredSize(new Dimension(1100, 800));
            frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        }

        public void needPasswordChange()
        {
            // put your code here
            change = true;
        }

        public void actionPerformed(ActionEvent e) 
        {
            //...Get information from the action event...
            //...Display it in the text area...

            if(e.getSource() == nameTextField || e.getSource() == start)
            {
                if(nameTextField.getText().equals(user.getPassword()))
                {
                    frame.setVisible(false);
                    JOptionPane.showMessageDialog(frame,
                        "Welcome Back " + user.getName() + " :))");
                    if(change == true)
                    {
                        UserSettings u = new UserSettings();
                        u.changePassword();
                        change = false;
                    }
                    refresh();
                }
                else
                {
                    JOptionPane.showMessageDialog(frame,
                        "Wrong Password. Please Try Again.");
                }

            }
        }

    }
}
