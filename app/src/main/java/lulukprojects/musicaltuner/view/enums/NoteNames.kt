package lulukprojects.musicaltuner.view.enums

import lulukprojects.musicaltuner.view.Interface.IKnowNextNoteName
import lulukprojects.musicaltuner.view.Interface.IKnowPreviousNoteName

enum class NoteNames : IKnowNextNoteName, IKnowPreviousNoteName {
    C {
        override fun getNextNoteName(): NoteNames {
            return CSharp
        }
        override fun getPreviousNoteName(): NoteNames {
            return B
        }
    },
    CSharp {
        override fun getNextNoteName(): NoteNames {
            return D
        }
        override fun getPreviousNoteName(): NoteNames {
            return C
        }

        override fun toString(): String {
            return "C#"
        }
    },
    D {
        override fun getNextNoteName(): NoteNames {
            return DSharp
        }
        override fun getPreviousNoteName(): NoteNames {
            return CSharp
        }
    },
    DSharp {
        override fun getNextNoteName(): NoteNames {
            return E
        }
        override fun getPreviousNoteName(): NoteNames {
            return D
        }
        override fun toString(): String {
            return "D#"
        }
    },
    E {
        override fun getNextNoteName(): NoteNames {
            return F
        }
        override fun getPreviousNoteName(): NoteNames {
            return DSharp
        }
    },
    F {
        override fun getNextNoteName(): NoteNames {
            return FSharp
        }
        override fun getPreviousNoteName(): NoteNames {
            return E
        }
    },
    FSharp {
        override fun getNextNoteName(): NoteNames {
            return G
        }
        override fun getPreviousNoteName(): NoteNames {
            return F
        }
        override fun toString(): String {
            return "F#"
        }
    },
    G {
        override fun getNextNoteName(): NoteNames {
            return GSharp
        }
        override fun getPreviousNoteName(): NoteNames {
            return FSharp
        }
    },
    GSharp {
        override fun getNextNoteName(): NoteNames {
            return A
        }
        override fun getPreviousNoteName(): NoteNames {
            return G
        }
        override fun toString(): String {
            return "G#"
        }
    },
    A {
        override fun getNextNoteName(): NoteNames {
            return ASharp
        }
        override fun getPreviousNoteName(): NoteNames {
            return GSharp
        }
    },
    ASharp {
        override fun getNextNoteName(): NoteNames {
            return B
        }
        override fun getPreviousNoteName(): NoteNames {
            return A
        }
        override fun toString(): String {
            return "A#"
        }
    },
    B {
        override fun getNextNoteName(): NoteNames {
            return C
        }
        override fun getPreviousNoteName(): NoteNames {
            return ASharp
        }
    },
}