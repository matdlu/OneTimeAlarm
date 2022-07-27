package pl.ascendit.onetimealarm.able

interface Navigable {
    enum class Destination {
        About, Settings,
        Alarm
    }
    fun navigate(to: Destination)
}
