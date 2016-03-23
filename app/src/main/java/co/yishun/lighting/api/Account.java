package co.yishun.lighting.api;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Carlos on 2016/3/23.
 */
public interface Account {

    enum Gender {
        @SerializedName("f")
        FEMALE,
        @SerializedName("m")
        MALE,
        @SerializedName("n")
        OTHER;

        public static Gender format(String s) {
            switch (s) {
                case "f":
                    return FEMALE;
                case "m":
                    return MALE;
                default:
                    return OTHER;
            }
        }

        public static Gender format(int i) {
            switch (i) {
                case 0:
                    return FEMALE;
                case 1:
                    return MALE;
                default:
                    return OTHER;
            }
        }

        @Override
        public String toString() {
            switch (this) {
                case FEMALE:
                    return "f";
                case MALE:
                    return "m";
                default:
                    return "n";
            }
        }

        public int toInt() {
            switch (this) {
                case FEMALE:
                    return 0;
                case MALE:
                    return 1;
                default:
                    return 2;
            }
        }

    }
}
