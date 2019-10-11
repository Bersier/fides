public class CodingExercise {

    public static void main(String[] args) {
        final String[] strings = {"geeksforgeeks", "geeks", "geek", "geezer"};
        System.out.println(solution(strings));
    }

    private static String solution(String[] strings) {
        assert strings.length > 0;
        final String shortest = shortest(strings);
        final StringBuilder answer = new StringBuilder();
        for (int position = 0; position < shortest.length(); position++) {
            char current = shortest.charAt(position);
            for (String string : strings) {
                if (string.charAt(position) != current) {
                    return answer.toString();
                }
            }
            answer.append(current);
        }
        return answer.toString();
    }

    private static String shortest(String[] strings) {
        assert strings.length > 0;
        String candidate = strings[0];
        int candidateLength = candidate.length();
        for (String string : strings) {
            if (string.length() < candidateLength) {
                candidate = string;
                candidateLength = string.length();
            }
        }
        return candidate;
    }
}
