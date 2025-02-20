class TokenTracker {
    private var currentPosition: Int = 0 // Starting position of the token

    // Function to move the token
    fun moveToken(spaces: Int): Int {
        currentPosition += spaces
        return currentPosition
    }

    // Function to get the current location of the token
    fun getCurrentLocation(): Int {
        return currentPosition
    }
}