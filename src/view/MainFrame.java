package view;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import Controller.Worker;
import model.Customer;
import model.Parcel;
import model.ParcelMap;
import model.QueueofCustomers;

public class MainFrame extends JFrame {
    private QueueofCustomers queue;
    private ParcelMap parcels;
    private JTextArea customerListArea;
    private JTextArea parcelListArea;
    private JTextArea feeListArea;
    private Worker worker;

    public MainFrame(QueueofCustomers queue, ParcelMap parcels, Worker worker) {
        if (queue == null || parcels == null || worker == null) {
            throw new IllegalArgumentException("Queue, Parcels, and Worker must not be null.");
        }
        this.queue = queue;
        this.parcels = parcels;
        this.worker = worker;

        setTitle("Depot Worker System");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Initialize text areas
        customerListArea = new JTextArea(10, 30);
        customerListArea.setEditable(false);
        parcelListArea = new JTextArea(10, 30);
        parcelListArea.setEditable(false);
        feeListArea = new JTextArea(10, 30);
        feeListArea.setEditable(false);
        

        // Add components to the frame
        JPanel panel = new JPanel(new GridLayout(3, 1));
        panel.add(new JScrollPane(customerListArea));
        panel.add(new JScrollPane(parcelListArea));
        panel.add(new JScrollPane(feeListArea));
        add(panel, BorderLayout.CENTER);

        loadData();
    }

    public void loadData() {
        // Ensure thread safety
        SwingUtilities.invokeLater(() -> {
            // Load customer data
            customerListArea.setText("");
            if (queue.getQueue() != null) {
                for (Customer customer : queue.getQueue()) {
                    customerListArea.append(customer.getName() + "\n");
                }
            }

            // Load parcel data
            parcelListArea.setText("");
            Map<String, Parcel> parcelMap = parcels.getAllParcels();
            if (parcelMap != null) {
                for (Parcel parcel : parcelMap.values()) {
                    parcelListArea.append(parcel.getId() + ": " + parcel.getDestination() + "\n");
                }
            }

            // Load fee data
            feeListArea.setText("");
            if (queue.getQueue() != null) {
                for (Customer customer : queue.getQueue()) {
                    double fee = worker.calculateFee(customer);
                    feeListArea.append(customer.getName() + " Fee: $" + fee + "\n");
                }
            }
        });
    }
}
