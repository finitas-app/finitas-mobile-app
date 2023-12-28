package pl.finitas.app.core.domain

interface Nameable {
    val name: String
}

interface NameableCollection<T: Nameable>: Nameable {
    val elements: List<T>
}
