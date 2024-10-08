import cv2
from yolov8 import YOLOv8
import numpy as np
from ultralytics import YOLO
import DetectIPlateNumber
import Config
import requests
from datetime import datetime
import requests

url = Config.domain+'api/valid_vehicles'  # Replace with your actual URL

global all_valid_vehicles, todays_incoming_vehicles, all_invalid_incoming_vehicles,valid_vehicles
all_valid_vehicles = []
todays_incoming_vehicles = []
all_invalid_incoming_vehicles = []
valid_vehicles = {}





def addLog(data,type):

    url = Config.domain+'api/save_vehicle_log'  # Replace with your actual URL

    data = {
        'vehicle_no': data['vehicle_no'],
        'type':  data['type'],
        'owner_name': data['owner_name'],
        'owner_email': data['owner_email'],
        'owner_address': data['owner_address'],
        'vehicle_image': data['vehicle_image'],  # Example: replace with actual file path or URL
        'is_valid': 'Yes',
        'order_number': '',
        'cam_type': type
    }

    try:
        response = requests.post(url, json=data)
        if response.status_code == 201:
            print("Vehicle log saved successfully.")
            todays_incoming_vehicles.append(data['vehicle_no'])
        else:
            print(f"Failed to save vehicle log. Status code: {response.status_code}")
            print(response.json())  # Print error message if available
    except requests.exceptions.RequestException as e:
        print(f"Error saving vehicle log: {e}")

def updateLog(data,type):
    

    url = Config.domain+'api/update_vehicle_log/'+data['vehicle_no']
    
    data = {
        'exit_date_time': datetime.now().strftime('%Y-%m-%d %H:%M:%S')  # Include exit_date_time for update API
    }

    try:
        response = requests.put(url, json=data)
        if response.status_code == 200:
            print("Vehicle log updated successfully.")
        else:
            print(f"Failed to update vehicle log. Status code: {response.status_code}")
            print(response.json())  # Print error message if available
    except requests.exceptions.RequestException as e:
        print(f"Error updating vehicle log: {e}")

def invalidLog(vehicle_no,type):
 
    url = Config.domain+'api/invalid_vehicles' 
    
    data = {
        'vehicle_no': vehicle_no,
        'cam_type': type,
        'is_valid':'No',
    }

    try:
        # Sending POST request to the API endpoint
        response = requests.post(url, json=data)

        # Checking the response status code
        if response.status_code == 201:
            all_invalid_incoming_vehicles.append(vehicle_no)
            todays_incoming_vehicles.append(vehicle_no)
            print('Invalid vehicle record added successfully.')
        else:
            print(f'Error: {response.status_code} - {response.json()}')
    except requests.exceptions.RequestException as e:
        print(f'Request error: {e}')

def updateInvalidLog(vehicle_no,type):

    # API endpoint URL
    url = Config.domain+'api/update_invalid_vehicle/'+vehicle_no  # Replace with actual URL and vehicle number

    # Example JSON data for the request
    data = {
        'is_valid': 'Yes',
        }

    try:
        # Sending PUT request to the API endpoint
        response = requests.put(url, json=data)

        # Checking the response status code
        if response.status_code == 200:
            all_invalid_incoming_vehicles.append(vehicle_no)
            todays_incoming_vehicles.append(vehicle_no)
            print('Invalid vehicle record updated and logged successfully.')
        else:
            print(f'Error: {response.status_code} - {response.json()}')
    except requests.exceptions.RequestException as e:
        print(f'Request error: {e}')



def detect_vehicles(frame, model, yolov8_licence_plate,type_cam):
    # Read the frame
    img_copy = frame.copy()
    if frame is None:
        print("Error: Unable to load frame.")
        return

    # Perform detection
    results = model.predict(frame)

    # Vehicle class IDs (YOLOv8 class IDs for car, bus, and truck)
    vehicle_class_ids = [1, 2, 5, 7]

    all_detected_plate_numbers = []

    # Process results
    for result in results:
        for detection in result.boxes:  # Access the bounding boxes
            # Extract bounding box coordinates, confidence score, and class ID
            x1, y1, x2, y2 = detection.xyxy[0]  # Bounding box coordinates
            confidence = detection.conf[0]  # Confidence score
            class_id = detection.cls[0]  # Class ID

            # Filter out low-confidence detections and non-vehicle classes
            if confidence > 0.5 and class_id in vehicle_class_ids:
                x1, y1, x2, y2 = map(int, detection.xyxy[0].tolist())  # Convert to integer list
                cropped_vehicle = frame[y1:y2, x1:x2]

                x1, y1, x2, y2 = detection.xyxy[0]  # Bounding box coordinates

                cv2.rectangle(frame, (int(x1), int(y1)), (int(x2), int(y2)), (0, 255, 0), 2)
                label = f"Vehicle: {confidence:.2f}"
                cv2.putText(frame, label, (int(x1), int(y1) - 10), cv2.FONT_HERSHEY_SIMPLEX, 0.5, (0, 255, 0), 2)

    boxes, scores, class_ids = yolov8_licence_plate(frame)
    frame, is_normal, is_moderate, is_critical, is_minor = yolov8_licence_plate.draw_detections(frame)

    all_detected_plate_numbers = DetectIPlateNumber.detect_license_plates(img_copy)

    # Text to be written
    text = "Vehicle No's. : " + ', '.join(map(str, all_detected_plate_numbers))

    # Font settings
    font = cv2.FONT_HERSHEY_SIMPLEX
    font_scale = 0.8
    font_thickness = 2

    # Calculate the width and height of the text box
    (text_width, text_height), baseline = cv2.getTextSize(text, font, font_scale, font_thickness)

    # Coordinates for the text
    x, y = 30, 30

    # Draw the rectangle and text in two lines
    cv2.rectangle(frame, (x, y - text_height - baseline), (x + text_width, y + baseline), (255, 255, 255), cv2.FILLED)
    cv2.putText(frame, text, (x, y), font, font_scale, (0, 0, 0), font_thickness)


    for detected_plat_number in all_detected_plate_numbers:
        if detected_plat_number in todays_incoming_vehicles:pass
        else:
            if detected_plat_number in valid_vehicles.keys():
                if type_cam=="Entry Gate":
                    addLog(valid_vehicles[detected_plat_number],type_cam)
                else:
                    updateLog(valid_vehicles[detected_plat_number],type_cam)
            else:                
                if type_cam=="Entry Gate":
                    invalidLog(detected_plat_number,type_cam)
                else:
                    updateInvalidLog(detected_plat_number,type_cam)

    return frame

def video_based_prediction(video_path,type_cam):

    global all_valid_vehicles, todays_incoming_vehicles, all_invalid_incoming_vehicles,valid_vehicles
    all_valid_vehicles = []
    todays_incoming_vehicles = []
    all_invalid_incoming_vehicles = []
    valid_vehicles = {}


    # Load YOLOv8 model
    model = YOLO(r'model/yolov8s.pt')  # Use the appropriate model file
    yolov8_licence_plate = YOLOv8(r"model/best.onnx", confidence_threshold=0.2, iou_threshold=0.3)

    # Open video file
    cap = cv2.VideoCapture(video_path)

    if not cap.isOpened():
        print("Error: Unable to open video file.")
        return

    frame_count = 0

    while cap.isOpened():
        ret, frame = cap.read()
        if not ret:
            break

        # Process every 5th frame

        if str(video_path)=='0':
            if frame_count % Config.skip_frames_camera == 0:
                frame = detect_vehicles(frame, model, yolov8_licence_plate,type_cam)

                # Display the frame with detections
                cv2.imshow("Vehicle Detection", frame)
                
                # Break the loop on 'q' key press
                if cv2.waitKey(1) & 0xFF == ord('q'):
                    break        
        else:
            if frame_count % Config.skip_frames_video == 0:
                frame = detect_vehicles(frame, model, yolov8_licence_plate,type_cam)

                # Display the frame with detections
                cv2.imshow("Vehicle Detection", frame)
                
                # Break the loop on 'q' key press
                if cv2.waitKey(1) & 0xFF == ord('q'):
                    break

        frame_count += 1

    # Release the video capture object and close all windows
    cap.release()
    cv2.destroyAllWindows()

# # Provide the path to your video
# video_path = r"S:\2021 Projects\ML Projects\Vehicle Tracking And Map Generating\final\V-routeModel-20230419T144949Z-001\V-routeModel\test_videos\video (2).mp4"
# # # video_path = 0
# video_based_prediction(video_path,'Entry Gate')
