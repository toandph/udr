package architectgroup.fact.util;

import org.jetbrains.annotations.NotNull;

/**
 * Created by Toan Dang, from Architect Group Inc.
 * User: Toandph
 * Date: 3/22/13
 * Time: 1:31 PM
 */
public enum Status {
    ANALYZE {
        @NotNull
        public String toString() {
            return "Analyze";
        }
    },
    IGNORE{
        @NotNull
        public String toString() {
            return "Ignore";
        }
    },
    NOT_A_PROBLEM {
        @NotNull
        public String toString() {
            return "Not a problem";
        }
    },
    FIX {
        @NotNull
        public String toString() {
            return "Fix";
        }
    },
    FIX_IN_NEXT_RELEASE {
        @NotNull
        public String toString() {
            return "Fix in next release";
        }
    },
    FIX_IN_LATER_RELEASE {
        @NotNull
        public String toString() {
            return "Fix in later release";
        }
    },
    DEFER {
        @NotNull
        public String toString() {
            return "Defer";
        }
    },
    FILTER {
        @NotNull
        public String toString() {
            return "Filter";
        }
    }
}
