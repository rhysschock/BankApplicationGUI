package guis;

import db_objs.User;

import javax.swing.*;

/* Creating an abstract class helps to set up the blueprint for all the GUIs as they
will all be the same size.*/

public abstract class BaseFrame extends JFrame {
    protected User user;

    public BaseFrame(String title){
        initialize(title);
    }
    public BaseFrame(String title, User user){
        this.user = user;
        initialize(title);
    }
    private void initialize(String title) {
        //add title to bar
        setTitle(title);

        //set size of window
        setSize(420,600);

        //terminate project on gui close
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        //absolute layout
        setLayout(null);

        //gui in center of screen
        setLocationRelativeTo(null);

        //prevent gui from being resized
        setResizable(false);

        //call on subclasses' addGUIComponent()
        addGUIComponents();
    }
    protected abstract void addGUIComponents();
}
