// David Hranicky
// COP4520

import java.util.Scanner;

class BirthdayParty{
    public static void main(String[] args){

        Scanner sc = new Scanner(System.in);
        int numGuests;

        System.out.println("How many guests are attending the birthday party?");
        numGuests = sc.nextInt();

        sc.close();
    }
}