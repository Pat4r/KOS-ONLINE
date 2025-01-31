package kos_online.config;

public class Messages {
    // Success messages
    public static final String SUCCESS_SAVE = "Data berhasil disimpan!";
    public static final String SUCCESS_UPDATE = "Data berhasil diupdate!";
    public static final String SUCCESS_DELETE = "Data berhasil dihapus!";
    public static final String SUCCESS_REGISTER = "Akun berhasil dibuat!";
    public static final String SUCCESS_PAYMENT = "Pembayaran berhasil disubmit!";
    
    // Error messages
    public static final String ERROR_REQUIRED_FIELDS = "Mohon lengkapi semua field!";
    public static final String ERROR_INVALID_LOGIN = "Username atau password salah!";
    public static final String ERROR_USERNAME_EXISTS = "Username sudah digunakan!";
    public static final String ERROR_INVALID_PRICE = "Harga harus berupa angka!";
    public static final String ERROR_INVALID_DATE = "Format tanggal salah. Gunakan format dd/MM/yyyy";
    public static final String ERROR_ROOM_OCCUPIED = "Tidak dapat mengubah kamar yang sedang terisi!";
    public static final String ERROR_NO_PAYMENT_PROOF = "Mohon upload bukti pembayaran terlebih dahulu";
    public static final String ERROR_FILE_TOO_LARGE = "Ukuran file terlalu besar (maksimal 5MB)";
    public static final String ERROR_INVALID_FILE_TYPE = "Format file tidak didukung";
    
    // Warning messages
    public static final String WARN_SELECT_ROOM = "Pilih kamar terlebih dahulu";
    public static final String WARN_ACCEPT_TERMS = "Silakan setuju dengan peraturan dan ketentuan";
    public static final String WARN_CONFIRM_DELETE = "Yakin ingin menghapus data ini?";
    public static final String WARN_CONFIRM_LOGOUT = "Apakah anda yakin ingin logout?";
    
    // Info messages
    public static final String INFO_NO_PAYMENT_PROOF = "Tidak ada bukti pembayaran untuk penyewa ini";
}