import java.util.*;

public class Main {

    // Class to store betting history per round
    static class BetHistory {
        double betAmount;
        double crashPoint;
        double cashedOutAt; // 0 if not cashed out
        double winnings;

        BetHistory(double betAmount, double crashPoint, double cashedOutAt, double winnings) {
            this.betAmount = betAmount;
            this.crashPoint = crashPoint;
            this.cashedOutAt = cashedOutAt;
            this.winnings = winnings;
        }

        public String toString() {
            if (cashedOutAt > 0) {
                return String.format("Bet: R%.2f | Cashed out at: x%.2f | Winnings: R%.2f", betAmount, cashedOutAt, winnings);
            } else {
                return String.format("Bet: R%.2f | 💥 Crashed at: x%.2f | Lost: R%.2f", betAmount, crashPoint, betAmount);
            }
        }
    }

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();
        List<BetHistory> history = new ArrayList<>();

        System.out.println("🎮 Welcome to Aviator Game!");
        System.out.print("Enter your balance: R");
        double balance = scanner.nextDouble();

        boolean playing = true;

        while (playing && balance > 0) {
            System.out.println("\nYour current balance: R" + balance);
            System.out.print("Enter your bet amount: R");
            double bet = scanner.nextDouble();

            if (bet > balance) {
                System.out.println("⚠️ Not enough balance. Try again.");
                continue;
            }

            double crashPoint = 1 + (random.nextDouble() * 5); // Random crash between 1.0x and 6.0x
            double multiplier = 1.00;
            boolean cashedOut = false;
            double cashedOutAt = 0.0;
            double winnings = 0.0;

            System.out.println("🛫 Plane is taking off... Press [Enter] to cash out before it crashes!");

            scanner.nextLine(); // clear buffer

            while (multiplier < crashPoint) {
                Thread.sleep(1000); // Delay 1 second
                multiplier += 0.10 + (random.nextDouble() * 0.30); // Increment multiplier randomly
                System.out.printf("Multiplier: x%.2f\n", multiplier);

                if (System.in.available() > 0) {
                    scanner.nextLine(); // Catch Enter
                    cashedOut = true;
                    cashedOutAt = multiplier;
                    winnings = bet * multiplier;
                    balance += winnings - bet;
                    System.out.printf("✅ You cashed out at x%.2f and won R%.2f!\n", cashedOutAt, winnings);
                    break;
                }
            }

            if (!cashedOut) {
                System.out.printf("💥 Plane crashed at x%.2f! You lost R%.2f\n", crashPoint, bet);
                balance -= bet;
            }

            // Record round in history
            history.add(new BetHistory(bet, crashPoint, cashedOutAt, winnings));

            System.out.print("\nDo you want to play again? (yes/no): ");
            String again = scanner.next().toLowerCase();
            playing = again.equals("yes");
        }

        // Print betting history
        System.out.println("\n📊 Betting History:");
        for (int i = 0; i < history.size(); i++) {
            System.out.println("Round " + (i + 1) + ": " + history.get(i));
        }

        System.out.printf("\n🎮 Game Over. Final Balance: R%.2f\n", balance);
        scanner.close();
    }
}
