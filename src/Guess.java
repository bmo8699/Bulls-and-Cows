import java.util.*;

/*
 * You need to implement an algorithm to make guesses
 * for a 4-digits number in the method make_guess below
 * that means the guess must be a number between [1000-9999]
 * PLEASE DO NOT CHANGE THE NAME OF THE CLASS AND THE METHOD
 */
public class Guess {

    public static Solver SOLVER = new Solver();
    public static boolean isFirstGuess = true;

    public static int make_guess(int hits, int strikes) {
        // just a dummy guess
        int myguess = 1000;

        /*
         * IMPLEMENT YOUR GUESS STRATEGY HERE
         */

        //First guess is always 1122, which is suggested by Donald Knuth in Mastermind(4,6)
        if (isFirstGuess){
            isFirstGuess = false;
            myguess = 1122; //Initial guess recommended by Donald Knuth
            SOLVER.setGuess(new Code(1122, true));
        } else {
            Response response = new Response(strikes, hits);
            SOLVER.processResponse(response);
            Code guess = SOLVER.guess();
            myguess = guess.getCode();
        }

        return myguess;
    }

    static class Solver {
        //Derive the size of sample space from upper, lower bound
        private static final int SAMPLE_SPACE_SIZE = (Code.MAX_CODE - Code.MIN_CODE) + 1;
        private Code guess = null; //Keep track of latest guess
        private final List<Code> impossibleCodes; //Keep track of impossible guesses through each step
        private final Set<Code> possibleCodes; //Contain possible guesses through each step

        public void setGuess(Code guess) {
            this.guess = guess;
        }

        /**
         * Solver constructor
         */
        public Solver() {
            impossibleCodes = new LinkedList<>(); //Init as linkedList to iterate
            possibleCodes = generateSampleSpace(); //Init as Set of sample space of codes following lexicographical order
        }

        /**
         * //Generate sample space
         * @return Set of Codes from sample space
         */
        private Set<Code> generateSampleSpace(){
            Set<Code> sampleSpace = new LinkedHashSet<>();
            for (int i = 0; i < SAMPLE_SPACE_SIZE; i++) {
                sampleSpace.add(new Code(i)); //Init code with code index
            }
            return sampleSpace;
        }


        /**
         * Process the response of the latest guess as following:
         *         1/Loop through the possibleCodes set
         *         2/Remove from possibleCodes any code that would not give the same response if it (the guess) were the code.
         *         3/Add the recently removed code to impossibleCodes linkedList
         * @param response: Response object
         */
        public void processResponse(Response response) {
            if (guess == null) return;
            Iterator<Code> iterator = possibleCodes.iterator();
            while (iterator.hasNext()) {
                Code i = iterator.next();
                if (!guess.getResponse(i).equals(response)) {
                    impossibleCodes.add(i);
                    iterator.remove();
                }
            }
        }

        /**
         * Strategy to make guess
         * @return guess
         */
        public Code guess(){
            int minimumEliminated = -1;
            Code bestGuess = null;
            //Create a list of all codes in sample space (including both impossible and possible codes)
            List<Code> unusedCodes = new LinkedList<>(possibleCodes);
            unusedCodes.addAll(impossibleCodes);

            //MinMax part
            //For each code in the sample space
            for (Code arbitraryCode : unusedCodes) {
                //Create a new 2D miniMax table
                int[][] scoreTable = new int[Code.CODE_LENGTH + 1][Code.CODE_LENGTH + 1];
                //For each possibleCode
                for (Code possibleCode : possibleCodes) {
                    Response response = arbitraryCode.getResponse(possibleCode); //Compare with the temporary arbitrary code and get response
                    scoreTable[response.getStrikes()][response.getHits()]++; //Increase number of codes which have this response
                }
                //Find max in the table
                int maxScoreHits = -1;
                for (int[] row : scoreTable) {
                    for (int i : row) {
                        maxScoreHits = Integer.max(i, maxScoreHits);
                    }
                }
                int score = possibleCodes.size() - maxScoreHits; //Evaluate score
                //Update minimumEliminated and bestGuess
                if (score > minimumEliminated) {
                    minimumEliminated = score;
                    bestGuess = arbitraryCode;
                }
            }

            //Update guess
            guess = bestGuess;

            return bestGuess;
        }
    }


    /**
     * A class for code guess
     */
    static class Code {
        public static final int MIN_CODE = 1000;   //Minimum code possible
        public static final int MAX_CODE = 9999;   //Maximum code possible
        public static final int CODE_LENGTH = 4;  //According to the problem statement
        private final int code; //Code value

        public Code (int code, boolean isCodeVal){
            this.code = code;
        }

        /**
         * //Create a code with the a codeIndex in sample space
         * @param codeIndex
         */
        public Code(int codeIndex){
            //Evaluate code based on codeIndex with lexicographical order
            this.code = codeIndex + MIN_CODE;
        }

        public int getCode() {
            return code;
        }

        /**
         * Get response when compared with other code
         * @param otherCode: other code object being compared
         * @return response from the comparison
         */
        public Response getResponse(Code otherCode) {
            char des[] = Integer.toString(code).toCharArray();
            char src[] = Integer.toString(otherCode.code).toCharArray();

            int hits = 0;
            int strikes = 0;
            int none = 0;

            // process strikes
            for (int i = 0; i < 4; i++) {
                if (src[i] == des[i]) {
                    strikes++;
                    des[i] = 'a';
                    src[i] = 'a';
                }
            }
            // process hits
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    if (src[i] != 'a') {
                        if (src[i] == des[j]) {
                            hits++;
                            des[j] = 'a';
                            break;
                        }
                    }
                }
            }
            return new Response(strikes, hits);
        }

    }

    /**
     * A class to determine response when comparing to codes, with 1 being target and the other being guess.
     */
    static class Response {

        private final int strikes;
        private final int hits;

        public Response(int strikes, int white) {
            this.strikes = strikes;
            this.hits = white;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Response other = (Response) obj;
            if (this.strikes != other.strikes) {
                return false;
            }
            if (this.hits != other.hits) {
                return false;
            }
            return true;
        }


        public int getStrikes() {
            return strikes;
        }

        public int getHits() {
            return hits;
        }

    }
}


