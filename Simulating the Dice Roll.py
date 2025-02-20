import random

def roll_dice():
    die1 = random.randint(1, 6)
    die2 = random.randint(1, 6)
    return die1, die2

dice = roll_dice()
print(f"You rolled: {dice[0]} and {dice[1]} (Total : {sum(dice)})")