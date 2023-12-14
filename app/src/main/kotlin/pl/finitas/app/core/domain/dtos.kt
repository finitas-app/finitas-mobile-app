package pl.finitas.app.core.domain

interface Nameable {
    val name: String
}

interface NameableCollection: Nameable {
    val elements: List<Nameable>
}
