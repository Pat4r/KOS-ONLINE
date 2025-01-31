package kos_online.config;

public class Constants {
    // Application constants
    public static final String APP_NAME = "Piilp Kost";
    public static final String APP_VERSION = "1.0.0";
    
    // Window dimensions
    public static final int WINDOW_WIDTH = 1280;
    public static final int WINDOW_HEIGHT = 720;
    
    // User types
    public static final String USER_TYPE_OWNER = "Pemilik Kos";
    public static final String USER_TYPE_TENANT = "Pencari Kos";
    
    // Room status
    public static final String ROOM_STATUS_EMPTY = "Kosong";
    public static final String ROOM_STATUS_OCCUPIED = "Terisi";
    
    // Payment status
    public static final String PAYMENT_STATUS_WAITING = "Menunggu";
    public static final String PAYMENT_STATUS_PAID = "Dibayar";
    public static final String PAYMENT_STATUS_CANCELLED = "Dibatalkan";
    
    // File upload
    public static final String[] ALLOWED_IMAGE_EXTENSIONS = {"jpg", "jpeg", "png"};
    public static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB
    
    // Timer intervals
    public static final int REFRESH_INTERVAL = 5000; // 5 seconds
    public static final int QUICK_REFRESH_INTERVAL = 100; // 0.1 seconds
}