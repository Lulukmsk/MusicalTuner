package lulukprojects.musicaltuner.view.enums

import lulukprojects.musicaltuner.view.Interface.IKnowNextNoteName
import lulukprojects.musicaltuner.view.Interface.IKnowPreviousNoteName

enum class NoteName : IKnowNextNoteName, IKnowPreviousNoteName {
    C {
        override fun getNextNoteName(): NoteName {
            return CSharp
        }
        override fun getPreviousNoteName(): NoteName {
            return B
        }
    },
    CSharp {
        override fun getNextNoteName(): NoteName {
            return D
        }
        override fun getPreviousNoteName(): NoteName {
            return C
        }

        override fun toString(): String {
            return "C#"
        }
    },
    D {
        override fun getNextNoteName(): NoteName {
            return DSharp
        }
        override fun getPreviousNoteName(): NoteName {
            return CSharp
        }
    },
    DSharp {
        override fun getNextNoteName(): NoteName {
            return E
        }
        override fun getPreviousNoteName(): NoteName {
            return D
        }
        override fun toString(): String {
            return "D#"
        }
    },
    E {
        override fun getNextNoteName(): NoteName {
            return F
        }
        override fun getPreviousNoteName(): NoteName {
            return DSharp
        }
    },
    F {
        override fun getNextNoteName(): NoteName {
            return FSharp
        }
        override fun getPreviousNoteName(): NoteName {
            return E
        }
    },
    FSharp {
        override fun getNextNoteName(): NoteName {
            return G
        }
        override fun getPreviousNoteName(): NoteName {
            return F
        }
        override fun toString(): String {
            return "F#"
        }
    },
    G {
        override fun getNextNoteName(): NoteName {
            return GSharp
        }
        override fun getPreviousNoteName(): NoteName {
            return FSharp
        }
    },
    GSharp {
        override fun getNextNoteName(): NoteName {
            return A
        }
        override fun getPreviousNoteName(): NoteName {
            return G
        }
        override fun toString(): String {
            return "G#"
        }
    },
    A {
        override fun getNextNoteName(): NoteName {
            return ASharp
        }
        override fun getPreviousNoteName(): NoteName {
            return GSharp
        }
    },
    ASharp {
        override fun getNextNoteName(): NoteName {
            return B
        }
        override fun getPreviousNoteName(): NoteName {
            return A
        }
        override fun toString(): String {
            return "A#"
        }
    },
    B {
        override fun getNextNoteName(): NoteName {
            return C
        }
        override fun getPreviousNoteName(): NoteName {
            return ASharp
        }
    },
}