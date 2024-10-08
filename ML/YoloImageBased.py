import cv2 
from yolov8 import YOLOv8
import uuid
# Initialize yolov8 object detector
model_path = r"model/best.onnx"
yolov8_detector = YOLOv8(model_path, confidence_threshold=0.2, iou_threshold=0.3)


def detect(img_url):
    print(img_url)
    # Read image
    img = cv2.imread(img_url)

    # Detect Objects
    boxes, scores, class_ids = yolov8_detector(img)

    # Draw detections
    combined_img, predicted_class = yolov8_detector.draw_detections(img)
    
    # Generate a random filename using uuid
    random_filename = "uploaded_datasets/"+ str(uuid.uuid4()) + '.jpg'

    print(random_filename)
    # Save the combined image with the random filename
    cv2.imwrite(random_filename, combined_img)

    print("saved")


    cv2.namedWindow("Detected Objects", cv2.WINDOW_NORMAL)
    cv2.imshow("Detected Objects", combined_img)
    if cv2.waitKey(0) & 0xFF == ord('q'):
        cv2.destroyAllWindows()
        return
        
    # return random_filename, predicted_class
