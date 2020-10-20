package model;

import java.util.ArrayList;


public class Table {
    private Integer seats;
    private int tableNumber;
    private boolean occupied;
    private boolean ordered;
    private boolean paid;
    private boolean paidall;
    private ArrayList orders;
    private Bill bill;



    public Table(int tableNumber) {
        this.tableNumber = tableNumber;
        this.orders = new ArrayList<Order>();
        this.bill = new Bill();
        this.occupied = false;

        this.ordered = false;
        this.paid = false;
        this.seats = 0;
        this.paidall = true;
        if(this.bill.getServedOrders().size()>0){
            this.paidall = false;
        }

        if(seats>7){
            bill.setTip(true);
        }
    }

    public int getTableNumber()
    {
        return this.tableNumber;
    }



    public ArrayList<Order> getOrders()
    {
        return orders;
    }

    public Bill getBill()
    {
       return this.bill;
    }

    public void setPaid ()
    {
        this.paid = true;
    }

    public boolean isPaid()
    {
        return paid;
    }

    public void setOccupied()
    {
        this.occupied = true;
    }

    public boolean isOccupied()
    {
        return this.occupied;
    }

    public void setOrdered()
    {
        this.ordered = true;
    }

    public boolean isOrdered()
    {
        return this.ordered;
    }

    @Override
    public String toString() {
        String str = new String(String.valueOf(tableNumber));
        return str;
    }

    public void resetTable()
    {
        this.occupied = false;
        this.ordered = false;
        this.paid = false;
        this.seats = 0;
        this.orders.clear();
        this.bill = new Bill();
    }

    public void setSeats(Integer seats) {
        this.seats = seats;
        if(seats>7){
            bill.setTip(true);
        } else {
            bill.setTip(false);
        }
    }

    public Integer getSeats() {
        return seats;
    }

    public void payAllBill(){
        this.bill.getServedOrders().clear();
    }
}

