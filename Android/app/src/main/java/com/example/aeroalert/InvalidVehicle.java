package com.example.aeroalert;
public class InvalidVehicle {
    private int id;
    private String vehicle_no;
    private String type;
    private String is_valid;
    private String current_time;
    private String order_number;
    private String cam_type;
    private String parking_time;
    private String vehicle_image;

    // Default constructor
    public InvalidVehicle() {}

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getVehicleNo() { return vehicle_no; }
    public void setVehicleNo(String vehicle_no) { this.vehicle_no = vehicle_no; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getIsValid() { return is_valid; }
    public void setIsValid(String is_valid) { this.is_valid = is_valid; }

    public String getCurrentTime() { return current_time; }
    public void setCurrentTime(String current_time) { this.current_time = current_time; }

    public String getOrderNumber() { return order_number; }
    public void setOrderNumber(String order_number) { this.order_number = order_number; }

    public String getCamType() { return cam_type; }
    public void setCamType(String cam_type) { this.cam_type = cam_type; }

    public String getParkingTime() { return parking_time; }
    public void setParkingTime(String parking_time) { this.parking_time = parking_time; }

    public String getVehicleImage() { return vehicle_image; }
    public void setVehicleImage(String vehicle_image) { this.vehicle_image = vehicle_image; }

    @Override
    public String toString() {
        return "InvalidVehicle{" +
                "id=" + id +
                ", vehicle_no='" + vehicle_no + '\'' +
                ", type='" + type + '\'' +
                ", is_valid='" + is_valid + '\'' +
                ", current_time='" + current_time + '\'' +
                ", order_number='" + order_number + '\'' +
                ", cam_type='" + cam_type + '\'' +
                ", parking_time='" + parking_time + '\'' +
                ", vehicle_image='" + vehicle_image + '\'' +
                '}';
    }
}
