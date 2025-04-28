import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import com.mongodb.client.*;
import org.bson.Document;
import java.util.*;

public class BloodBankManagementSystem {

    public static void main(String[] args) {
        new HomePage();
    }
}

class HomePage extends JFrame {

    HomePage() {
        setTitle("Blood Bank Management System");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JButton donateButton = new JButton("Donate Blood");
        JButton requestButton = new JButton("Request Blood");
        JButton viewButton = new JButton("View Blood Stock");

        donateButton.addActionListener(e -> new DonateForm());
        requestButton.addActionListener(e -> new RequestForm());
        viewButton.addActionListener(e -> new ViewStock());

        setLayout(new GridLayout(3, 1, 10, 10));
        add(donateButton);
        add(requestButton);
        add(viewButton);

        setVisible(true);
    }
}

class DonateForm extends JFrame {
    DonateForm() {
        setTitle("Donate Blood");
        setSize(300, 300);
        setLocationRelativeTo(null);

        JLabel nameLabel = new JLabel("Name:");
        JTextField nameField = new JTextField(15);

        JLabel bloodLabel = new JLabel("Blood Group:");
        JTextField bloodField = new JTextField(15);

        JLabel contactLabel = new JLabel("Contact:");
        JTextField contactField = new JTextField(15);

        JButton submitButton = new JButton("Submit");

        submitButton.addActionListener(e -> {
            String name = nameField.getText();
            String blood = bloodField.getText();
            String contact = contactField.getText();

            MongoDBHelper.donateBlood(name, blood, contact);

            JOptionPane.showMessageDialog(this, "Donated Successfully!");
            dispose();
        });

        setLayout(new FlowLayout());
        add(nameLabel); add(nameField);
        add(bloodLabel); add(bloodField);
        add(contactLabel); add(contactField);
        add(submitButton);

        setVisible(true);
    }
}

class RequestForm extends JFrame {
    RequestForm() {
        setTitle("Request Blood");
        setSize(300, 300);
        setLocationRelativeTo(null);

        JLabel bloodLabel = new JLabel("Blood Group Needed:");
        JTextField bloodField = new JTextField(15);

        JButton requestButton = new JButton("Request");

        requestButton.addActionListener(e -> {
            String bloodGroup = bloodField.getText();
            // Here you can implement search logic later if you want
            JOptionPane.showMessageDialog(this, "Request submitted for " + bloodGroup);
            dispose();
        });

        setLayout(new FlowLayout());
        add(bloodLabel); add(bloodField);
        add(requestButton);

        setVisible(true);
    }
}

class ViewStock extends JFrame {
    ViewStock() {
        setTitle("Available Blood Stocks");
        setSize(400, 300);
        setLocationRelativeTo(null);

        String[] columns = {"Blood Group", "Units Available"};
        Map<String, Integer> bloodStock = new HashMap<>();

        // Fetch blood stock from database
        MongoCollection<Document> collection = MongoDBHelper.getCollection();
        MongoCursor<Document> cursor = collection.find().iterator();

        try {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                String bloodGroup = doc.getString("bloodGroup");
                bloodStock.put(bloodGroup, bloodStock.getOrDefault(bloodGroup, 0) + 1);
            }
        } finally {
            cursor.close();
        }

        // Convert Map to Array
        String[][] data = new String[bloodStock.size()][2];
        int i = 0;
        for (Map.Entry<String, Integer> entry : bloodStock.entrySet()) {
            data[i][0] = entry.getKey();
            data[i][1] = entry.getValue().toString();
            i++;
        }

        JTable table = new JTable(data, columns);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane);

        setVisible(true);
    }
}
