import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
            /*SCell c1 = new SCell(scanner.nextLine());
            System.out.println(c1.getData());
            System.out.println("Is valid form: " + c1.isForm(c1.getData()));
            System.out.println("Value is: " + c1.computeForm(c1.getData().replace("=", "")));
            */
        Ex2Sheet sheet = new Ex2Sheet(3, 3);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++){
                System.out.print(" | " + sheet.get(i, j).computeForm(sheet.get(i,j).getData().replace("=", "")) + " | ");
            }
            System.out.println();
        }
        System.out.println("Making some changes...");
        sheet.get(1, 1).setData("=1+2");
        sheet.get(1, 2).setData("=2+3");
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++){
                System.out.print(" | " + sheet.get(i, j).computeForm(sheet.get(i,j).getData().replace("=", "")) + " | ");
            }
            System.out.println();
        }
        System.out.println("B1 contains: " + sheet.get("B1").getData());
        System.out.println(sheet.transForm("=1+b1+b2+3"));
    }



    public static int[] getCellCoordinate(String cell){
        int row = Integer.parseInt(cell.substring(1));
        int collum = ((int) cell.charAt(0)) - 65;
        return new int[]{row, collum};
    }

}