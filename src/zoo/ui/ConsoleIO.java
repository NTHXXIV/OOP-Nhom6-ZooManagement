package zoo.ui;

import java.util.Scanner;

/** Console-only I/O for the main menu and generic input/output. No business logic, no persistence. */
public class ConsoleIO {

    public void printMainMenu() {
        System.out.println();
        System.out.println("===== HỆ THỐNG QUẢN LÝ VƯỜN THÚ =====");
        System.out.println("1. Thêm động vật");
        System.out.println("2. Xóa động vật");
        System.out.println("3. Sửa thông tin động vật");
        System.out.println("4. Tìm kiếm động vật");
        System.out.println("5. Hiển thị danh sách động vật");
        System.out.println("6. Lưu vào file");
        System.out.println("7. Tải từ file");
        System.out.println("0. Thoát");
        System.out.print("Chọn chức năng: ");
    }

    public int readChoice(Scanner sc) {
        try {
            return Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public void showMessage(String msg) {
        System.out.println(msg);
    }

    public void showError(String msg) {
        System.out.println("LỖI: " + msg);
    }

    public String promptText(Scanner sc, String label) {
        System.out.print(label + ": ");
        return sc.nextLine().trim();
    }
}
