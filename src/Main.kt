import kotlin.random.Random

fun main() {
    println("=== ИГРА 'БЫКИ И КОРОВЫ' ===")
    println("Компьютер загадал 4-значное число с неповторяющимися цифрами.")
    println("Число может начинаться с нуля!")
    println("Ваша задача - угадать это число.")
    println("Бык - цифра угадана и стоит на правильной позиции")
    println("Корова - цифра угадана, но стоит на неправильной позиции")
    println("Введите 'exit' для выхода из игры")
    println()
    
    val secretNumber = generateSecretNumber()
    var attempts = 0
    
    while (true) {
        print("Введите вашу попытку (4 цифры): ")
        val input = readLine()?.trim()
        
        if (input == "exit") {
            println("Игра завершена. Загаданное число было: ${secretNumber.joinToString("")}")
            break
        }
        
        if (input == null || input.length != 4) {
            println("Ошибка: введите ровно 4 цифры!")
            continue
        }
        
        val guess = input.map { it.toString().toIntOrNull() }
        if (guess.any { it == null }) {
            println("Ошибка: введите только цифры!")
            continue
        }
        
        val guessNumbers = guess.map { it!! }
        if (!isValidGuess(guessNumbers)) {
            println("Ошибка: все цифры должны быть разными!")
            continue
        }
        
        attempts++
        val result = calculateBullsAndCows(secretNumber, guessNumbers)
        
        println("Попытка $attempts: ${guessNumbers.joinToString("")}")
        println("Результат: ${result.first} быков, ${result.second} коров")
        println()
        
        if (result.first == 4) {
            println("🎉 Поздравляем! Вы угадали число за $attempts попыток!")
            break
        }
    }
}

/**
 * Генерирует случайное 4-значное число с неповторяющимися цифрами
 * Число может начинаться с нуля
 */
fun generateSecretNumber(): List<Int> {
    val digits = mutableListOf<Int>()
    val availableDigits = (0..9).toMutableList()
    
    repeat(4) {
        val randomIndex = Random.nextInt(availableDigits.size)
        val digit = availableDigits.removeAt(randomIndex)
        digits.add(digit)
    }
    
    return digits
}

/**
 * Проверяет, является ли введенное число корректным
 * (4 цифры, все разные)
 */
fun isValidGuess(guess: List<Int>): Boolean {
    return guess.size == 4 && guess.toSet().size == 4
}

/**
 * Подсчитывает количество быков и коров
 * @param secret загаданное число
 * @param guess попытка угадать
 * @return пара (быки, коровы)
 */
fun calculateBullsAndCows(secret: List<Int>, guess: List<Int>): Pair<Int, Int> {
    var bulls = 0
    var cows = 0
    
    // Подсчитываем быков (правильная позиция)
    for (i in secret.indices) {
        if (secret[i] == guess[i]) {
            bulls++
        }
    }
    
    // Подсчитываем коров (правильная цифра, неправильная позиция)
    val secretDigits = secret.toSet()
    val guessDigits = guess.toSet()
    val commonDigits = secretDigits.intersect(guessDigits)
    
    cows = commonDigits.size - bulls
    
    return Pair(bulls, cows)
}