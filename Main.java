//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Miscellaneous {
    constructor(name) {
        this.name = name;
    }

    interact(player, board) {
        if (this.name === "Go") {
            return;
        } else if (this.name === "Income Tax") {
            console.log(`${player.name} pays Income Tax of $200.`);
            player.money -= 200;
        } else if (this.name === "Jail") {
            console.log("Just Visiting!");
        } else if (this.name === "Free Parking") {
            console.log("Nothing interesting happens.");
        } else if (this.name === "Go To Jail") {
            board.go_to_jail(player);
        } else if (this.name === "Luxury Tax") {
            console.log(`${player.name} pays Luxury Tax of $75.`);
            player.money -= 75;
        }
    }
}