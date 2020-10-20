package model;
import javafx.scene.control.Alert;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Restaurant {

    private String name;

    /**
     * An ArrayList that stores all the employees.
     */
    private ArrayList<Receiver> employees = new ArrayList<>();

    /**
     * A variable used to access the restaurantManager.
     */
    private RestaurantManager restaurantManager;
    private TableManager tableManager;


    private static int serverCount = 0;

    private static int cookCount = 0;

    private static int managerCount = 0;

    private final String fileName = "employees.txt";

    /**
     * Initializes a Restaurant.
     */
    public Restaurant(String name) throws IOException {
        this.name = name;
        restaurantManager = new RestaurantManager(this.name);
        this.tableManager = tableManager;
        readEmployeeTextFile();
        writeEmployeeTextFile();
    }

    public int getServerCount ()
    {
        return serverCount;
    }

    public int getCookCount ()
    {
        return cookCount;
    }

    public int getManagerCount ()
    {
        return managerCount;
    }

    public void addServer(String name)
    {
        addEmployee(restaurantManager.createServer(name,serverCount));
        serverCount++;
    }

    public void addCook(String name)
    {
        addEmployee(restaurantManager.createCook(name,cookCount));
        cookCount++;
    }

    public void addManager(String name)
    {
        addEmployee(restaurantManager.createManager(name,managerCount));
        managerCount++;
    }

    public boolean readEmployeeTextFile () throws IOException
    {
        try (BufferedReader fileReader = new BufferedReader(new FileReader(fileName))) {
            String line = fileReader.readLine(); //skipping insignificant lines
            line = fileReader.readLine();//skipping insignificant lines
            line = fileReader.readLine();
            serverCount = Integer.parseInt(line.split(" = ")[1].trim());
            line = fileReader.readLine();
            cookCount = Integer.parseInt(line.split(" = ")[1].trim());
            line = fileReader.readLine();
            managerCount = Integer.parseInt(line.split(" = ")[1].trim());
            line = fileReader.readLine();//Skipping insignificant Lines
            line = fileReader.readLine();//Skipping insignificant Lines

            int i = 0;
            String[] testBlank = null;
            while (line != null) {
                line = fileReader.readLine();
                if (line != null && line.split(",").length==4) {
                    processLine(line);
                    i++;
                } else {
                    break;
                }
            }
        } catch (IOException e)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Warning");
            alert.setContentText("IOException when trying to read employee.txt");
            alert.showAndWait();
        }
        return false;
    }

    private void processLine(String line)
    {
        String[] lineArray = line.split(",");
        if (lineArray.length == 4) {
            String type = lineArray[0].trim().split(":")[0];
            int idNum = Integer.parseInt(lineArray[0].split(":")[1].trim());
            String name = lineArray[2].trim();

            switch (type) {
                case "S":
                    addEmployee(restaurantManager.createServer(name, idNum));
                    break;
                case "C":
                    addEmployee(restaurantManager.createCook(name, idNum));
                    break;
                case "M":
                    addEmployee(restaurantManager.createManager(name, idNum));
                    break;
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Syntax Error");
            alert.setHeaderText("Warning");
            alert.setContentText("Please re-check the syntax of employee.txt");
            alert.showAndWait();
        }
    }

    public boolean writeEmployeeTextFile ()
    {
        boolean ret = false;
        File fNew = new File(fileName);
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        try {
            FileWriter fStream = new FileWriter(fNew, false);
            BufferedWriter out = new BufferedWriter(fStream);
            out.write("[Employee File | "+ dateFormat.format(date)+" | "+this.name+"]");
            out.newLine();
            out.newLine();
            String formatStr = "%-5s %-10s %-10s%n";
            out.write("Server Count = "+serverCount);
            out.newLine();
            out.write("Cook Count = "+cookCount);
            out.newLine();
            out.write("Manager Count = "+managerCount);
            out.newLine();
            out.newLine();
            out.write(String.format(formatStr, "ID", "Role", "Name"));

            ArrayList<Receiver> allServers = getAllEmployeesOfType("S");

            for (Receiver emp : allServers)
            {
                String id = emp.getEmployeeId();
                String role  = "Server";
                String name = emp.getName();

                out.write(String.format(formatStr, id+"," , role+"," , name+","));
            }

            ArrayList<Receiver> allCooks = getAllEmployeesOfType("C");
            for (Receiver emp : allCooks)
            {
                String id = emp.getEmployeeId();
                String role  = "Cook";
                String name = emp.getName();

                out.write(String.format(formatStr, id+"," , role+"," , name+","));
            }

            ArrayList<Receiver> allManagers = getAllEmployeesOfType("M");
            for (Receiver emp : allManagers)
            {
                String id = emp.getEmployeeId();
                String role  = "Manager";
                String name = emp.getName();

                out.write(String.format(formatStr, id+"," , role+"," , name+","));
            }

            ret = true;
            out.close();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Warning");
            alert.setContentText("IOException when writing employee.txt");
            alert.showAndWait();
            ret = false;
        }
        return ret;
    }

    /**
     * Adds a new employee to the employee ArrayList.
     *
     * @param employee the employee object that wants to be added to the ArrayList.
     */
    private void addEmployee(Object employee)
    {
        employees.add((Receiver) employee);
    }

    public RestaurantManager getRestaurantManager()
    {
        return restaurantManager;
    }

    public Receiver getEmployee(String empID)
    {
        Receiver ret = null;
        for (Receiver r : employees)
        {
            if (r.getEmployeeId().equals(empID))
            {
                ret = r;
            }
        }
        return ret;
    }

    public void listEmployees()
    {
        for (Receiver e : employees)
        {
            System.out.println("Name: "+e.getName()+ " | ID: "+e.getEmployeeId());
        }
    }

    public ArrayList getAllEmployeesOfType(String type)
    {
        ArrayList<Receiver> ret = new ArrayList();
        for (Receiver r : employees )
        {
            if (r.getEmployeeId().split(":")[0].equals(type))
            {
                ret.add(r);
            }
        }
        return ret;
    }

    public ArrayList getEmployees ()
    {
        return employees;
    }

    @Override
    public String toString() {
        return this.name;
    }

    public TableManager getTableManager() {
        return tableManager;
    }

}
