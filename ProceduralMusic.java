import java.util.*;

public class ProceduralMusic {

    // the 12 notes in western music
    private static final List<String> noteNames = Arrays.asList(
    "C", "Db", "D", "Eb", "E", "F", "Gb", "G", "Ab", "A", "Bb", "B");
    //for Sharps: (if not above look down type fix)
    private static final List<String> notesSharps = Arrays.asList(
    "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B");

    //dissonance values between intervals, aka a perfect 5th is not so dissonant while a tritone is very dissonant.
    //ordered based on distance to first index: "0.0", in this case 0.165 is the 5th and the 0.99 is the tritone.
    public static final List<Double> dissValues = Arrays.asList(
    0.0, 0.825, 0.66, 0.4125, 0.33, 0.2475, 0.99, 0.165, 0.5775, 0.495, 0.7425, 0.9075);

    //the complexity values for triads.
    //A bit different than dissonance because cmaj7add9 is more complex than cdim but not more dissonant.
    //the complexities are as follows:
    public static final List<Double> complValues = Arrays.asList(
                      0.0, 0.198, 0.396, 0.594, 0.792, 0.99);
    //(from left to right) maj, min,   sus2,  sus4,  dim,   aug.

    // for modesList/scales:
    public static final List<Integer> Major = Arrays.asList(2, 2, 1, 2, 2, 2, 1);
    public static final List<Integer> Minor = Arrays.asList(2, 1, 2, 2, 1, 2, 2);
    public static final List<Integer> Mixolydian = Arrays.asList(2, 2, 1, 2, 2, 1, 2);
    public static final List<List<?>> modesList = Arrays.asList(
        Arrays.asList("Major", Major), 
        Arrays.asList("Minor", Minor), 
        Arrays.asList("Mixolydian", Mixolydian));

    // for chord creation:
    // quality intervals:
    public static final List<Integer> maj = Arrays.asList(4, 3);
    public static final List<Integer> min = Arrays.asList(3, 4);
    public static final List<Integer> dim = Arrays.asList(3, 3);
    public static final List<Integer> sus2 = Arrays.asList(2, 5);
    public static final List<Integer> sus4 = Arrays.asList(5, 2);

    // quality list and name:(I realize later that I basically made a makeshift dictionary in the practical sense but havent bothered to change it into an actual dictionary *yet* since I would have to switch up some code. I may sometime though if need be...)
    public static final List<List<?>> qualityList = Arrays.asList(
        Arrays.asList("maj", maj), 
        Arrays.asList("min", min), 
        Arrays.asList("dim", dim), 
        Arrays.asList("sus2", sus2), 
        Arrays.asList("sus4", sus4));

    // add interval shortcuts: (intervals for each root is the same)
    // this half can be used to both add and omit:
    public static final int add1 = 0;
    public static final int addb2 = 1;
    public static final int add2 = 2;
    public static final int addShar2 = 3;
    public static final int addb3 = 3;
    public static final int add3 = 4;
    public static final int add4 = 5;
    public static final int addShar4 = 6;
    public static final int addb5 = 6;
    public static final int add5 = 7;
    public static final int addShar5 = 8;
    public static final int addb6 = 8;
    public static final int add6 = 9;
    public static final int addShar6 = 10;
    public static final int addb7 = 10;
    public static final int add7 = 11;

    // rest is for chord construction only (unless truly working octaves are in place, then yes use these for omits aswell. (also you might have to change the omit func)):
    public static final int addOctave = 12;
    public static final int add8 = 12;
    public static final int addb9 = 13;
    public static final int add9 = 14;
    public static final int addShar9 = 15;
    public static final int add11 = 17;
    public static final int addShar11 = 18;
    public static final int addb13 = 20;
    public static final int add13 = 21;
    public static final int addShar13 = 22;

    // adds list and name: (another makeshift dictionary...that i might turn into an actual dictionary)
    public static final List<List<?>> addsList = Arrays.asList(
    Arrays.asList("add1", add1),
    Arrays.asList("addb2", addb2),
    Arrays.asList("add2", add2),
    Arrays.asList("addShar2", addShar2),
    Arrays.asList("addb3", addb3),
    Arrays.asList("add3", add3),
    Arrays.asList("add4", add4),
    Arrays.asList("addShar4", addShar4),
    Arrays.asList("addb5", addb5),
    Arrays.asList("add5", add5),
    Arrays.asList("addShar5", addShar5),
    Arrays.asList("addb6", addb6),
    Arrays.asList("add6", add6),
    Arrays.asList("addShar6", addShar6),
    Arrays.asList("addb7", addb7),
    Arrays.asList("add7", add7),
    Arrays.asList("addOctave", addOctave),
    Arrays.asList("add8", add8),
    Arrays.asList("addb9", addb9),
    Arrays.asList("add9", add9),
    Arrays.asList("addShar9", addShar9),
    Arrays.asList("add11", add11),
    Arrays.asList("addShar11", addShar11),
    Arrays.asList("addb13", addb13),
    Arrays.asList("add13", add13),
    Arrays.asList("addShar13", addShar13)
);

    //Creating a note class
    static class Note {
        String name;
        int octave;
        int distance;

        //Note Constructor that takes in a name ex. "C#" or "Bb", and an octave ex. 4, or 6.
        public Note(String name, int octave) {
            this.name = name;
            this.octave = octave;
            this.distance = calculateDistance(name);
        }

        //This function is used for interval calculations and by extension, chord, scale and quality production.
        private int calculateDistance(String name) {
            int dist = noteNames.indexOf(name);
            if(dist == -1){
                dist = notesSharps.indexOf(name);
            }
            return dist;
        }
        //Used to calculate the distance between two notes, aka their interval (upper extensions appear as they are not as inbound. ex. Cmajadd9 has a D in a higher octave, so the distance between C and D in this case is not 2 but 14)
        public int distanceBetween(Note note) {
            int interval = Math.abs(distance - note.distance);
            if(Math.abs(octave-note.octave) != 0){
                interval+=12;
            }
            return interval;
        }        

            @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Note note = (Note) o;
            return octave == note.octave && Objects.equals(name, note.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, octave);
        }
    }
    //Creating a Quality class for triads only, aka. Cmaj7 has the "triad" quality of maj, and Csus2addb11 has the "triad" quality of sus2.
    static class Quality {
        List<Integer> intervals;
        String name;
        Double complexity;

        //Constructor takes in a list of intervals, and the name of the quality (aka. Quality(maj, "maj"), the first maj contains the intervals list above (the very top))
        public Quality(List<Integer> intervals, String name) {
            this.intervals = intervals;
            this.name = name;
            this.complexity = calcComplexity();
    
        }
        private Double calDissonance(){ //currently not working properly so I use the one in the Chord Class, though I dont think this version is ever needed but iguess it would be nice to have..?
            Double sum = 0.0;
            int rootFifth = 0;
            for(int i = 0; i< intervals.size(); i++){
                sum+= dissValues.get(intervals.get(i));
                rootFifth += intervals.get(i);
            }
            sum+= rootFifth;
            return sum;
        }

        //the previously mentioned complexity variable's base function. I call it a base function because when you add extensions and such the complexity will increase on top of the Quality's complexity.
        private Double calcComplexity() { //how? triads have base complexity, any add normally adds 0.1 but acoording to distance that increases
            Double comp= 0.0;
            for (int i = 0; i < qualityList.size(); i++) { //Quality list is above at the top, contains the complexity variables for each triad.
                String compare = (String) qualityList.get(i).get(0);
                if(compare.equals(this.name)){
                    comp = complValues.get(i);
                    break;
                }
            }
            return comp; //"add" complexities come in at the chord. So Cord.quality.complexity + add complexity. YEsss.
        }
    }

    //Creating a chord class
    static class Chord {
        Note root;
        Quality quality;
        List<Integer> add;
        List<Note> notes;

        //Takes in a root note the chord will be based on (C), a triad quality(maj), and any extra notes, in the form of intervals (add9), 
        //then based on the intervals the constructor creates a list on notes that are in the chord: [C, E, G, D(9)].
        public Chord(Note root, Quality quality, Integer... add) {
            this.root = root;
            this.quality = quality;
            this.add = Arrays.asList(add);

            List<Note> listOfNotes = new ArrayList<>();
            listOfNotes.add(root);
            int currentDistance = root.distance;
            for (int interval : quality.intervals) {
                currentDistance += interval;
                int noteIndex = currentDistance % 12;
                int octaveShift = currentDistance / 12;
                Note note = new Note(noteNames.get(noteIndex), root.octave + octaveShift);
                listOfNotes.add(note);
            }

            for (int interval : add) {
                currentDistance = root.distance + interval;
                int noteIndex = currentDistance % 12;
                int octaveShift = currentDistance / 12;
                Note note = new Note(noteNames.get(noteIndex), root.octave + octaveShift);
                listOfNotes.add(note);
            }

            this.notes = listOfNotes;
        }

        //an omit function to take out notes from the chord. so Cmaj9.omit(add5) -> [C, E, D] (instead of [C, E, G, D])
        public void omit(int interval) {
            int distanceToOmit = (root.distance + interval) % 12;
            notes.removeIf(note -> note.distance == distanceToOmit);
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            for (Note note : notes) {
                sb.append(note.name).append(note.octave).append(" ");
            }
            return sb.toString().trim();
        }

        //calculates the dissonance of every interval in the chord, so checks root with everything, then the second note with the rest than the third note with the rest etc.
        private Double calcDissonance(){
            Double sum = 0.0;
            for(int i = 0; i<this.notes.size()-1; i++){
                for(int j = i+1; j<this.notes.size(); j++){
                    int interval = notes.get(i).distanceBetween(notes.get(j));
                    Double disVal =dissValues.get(interval%12);
                    sum+=disVal;
                }
            }
            return sum; //subject to change may be turned into avg dissonance which will make probability stuff easier later on due to the avg being between 0.00-0.99
        }

        private void addToChord(Integer... add){
            for (int interval : add) {
                int currentDistance = root.distance + interval;
                int noteIndex = currentDistance % 12;
                int octaveShift = currentDistance / 12;
                
                if (interval > 12) {
                    octaveShift++;  // Ensure the extension note is at least an octave above the root
                }
        
                Note note = new Note(noteNames.get(noteIndex), root.octave + octaveShift);
                if (!notes.contains(note)) {
                    notes.add(note);
                }
            }
        }
    }

    //Defining a scale class
    static class Scale {
        Note tonic;
        List<Note> notes;
        List<Object> mode;
        List<List<?>> scaleQualities;

        //Takes in the mode (mode is a list containing the intervals of that mode, all given above), and turns it into actual notes for a given tonic note.
        //Ex. Scale(Arrays.asList(maj, "maj"), C) -> [C, D, E, F, G, A, B, C]: Notes of the C maj scale.
        public Scale(List<Object> mode, Note tonic) {
            this.tonic = tonic;
            this.mode = mode;
            this.notes = new ArrayList<>();
            this.notes.add(tonic);
            int trav = tonic.distance;
            @SuppressWarnings("unchecked")
            List<Integer> modulo = (List<Integer>) mode.get(1); // not a real term, temp var.
            int modesListize = modulo.size();

            for (int i = 0; i < modesListize-1; i++) {
                trav += modulo.get(i);
                if (trav > 11) {
                    trav -= 12;
                }
                this.notes.add(new Note(noteNames.get(trav), tonic.octave));
            }
            this.scaleQualities = this.calculateScaleQualities();
        }
        
        //Scale qualities refers to the fact that the triads made from a give scale always have the same qualities in order.
        //Ex. the qualities of maj scales: maj-min-min-maj-maj-min-dim (these are the qualities of chords that folow the roots from C to B for example; C is maj, B is dim.)
        public List<List<?>> calculateScaleQualities() {
            List<List<?>> modeQualities = new ArrayList<>();
            @SuppressWarnings("unchecked")
            List<Integer> modeIntervals = (List<Integer>) mode.get(1);
            for (int i = 0; i < modeIntervals.size(); i++) {
                List<Integer> modeTest = new ArrayList<>();
                modeTest.add(modeIntervals.get(i)+ modeIntervals.get((i + 1) % modeIntervals.size()));
                modeTest.add(modeIntervals.get((i+2)% modeIntervals.size()) + modeIntervals.get((i + 3) % modeIntervals.size()));
                for (List<?> quality : qualityList) {
                    @SuppressWarnings("unchecked")
                    List<Integer> qualityIntervals = (List<Integer>) quality.get(1);
                    if (modeTest.equals(qualityIntervals)) {
                        modeQualities.add(Arrays.asList(quality.get(0), qualityIntervals));
                        break;
                    }
                }
            }
            return modeQualities;
        }

        //Gets a given degree's quality in a scale (helper method for (noteToTriad) chord construction in the scale)
        @SuppressWarnings("unchecked")
        public Quality getQuality(int degree){
            List<Object> list = (List<Object>) scaleQualities.get(degree);
            List<Integer> intervals = (List<Integer>) list.get(1);
            String n = (String) list.get(0);
            Quality q = new Quality(intervals, n);
            return q;
        }

        //Takes a random note in (or out of) the scale and returns its appropriate triad. (if the note is out of the scale returns a triad formed from the next or previous degree quality that is in the scale (random (%50%50)))
        public Chord noteToTriad(Note n){
            int degree = this.notes.indexOf(n);
            if(degree == -1){
                Random rand = new Random();
                int direction = rand.nextInt(2); // generates either 0 or 1

                degree = (noteNames.indexOf(n.name));
                if(degree == -1){
                    degree = notesSharps.indexOf(n.name);
                    
                }
                degree =  (degree+11) % 12;
                Note halfStepLowerNote = new Note(noteNames.get(degree), n.octave);
                degree = this.notes.indexOf(halfStepLowerNote);

                if (direction == 1) {
                    degree += 1;
                }
            }

                System.err.println("The chosen degree this time is: " + degree);
            Quality q = getQuality(degree);
            Chord triad = new Chord(n, q);
            return triad;
        }
    }

    //Chooses a random Mode
    public static List<Object> chooseRandomMode() {
        Random rand = new Random();
        int number = rand.nextInt(modesList.size());
        return Arrays.asList(modesList.get(number).get(0), modesList.get(number).get(1));
    }

    //Chooses a random Note
    public static Note chooseRandomNote() {
        Random rand = new Random();
        int number = rand.nextInt(noteNames.size());
        return new Note(noteNames.get(number), rand.nextInt(8)); // Random octave for demonstration
    }


    //Test Code for now:
    public static void main(String[] args) {
        // Setting up a scale with a known mode and tonic
    Note tonic = new Note("C", 4);
    List<Object> majorMode = Arrays.asList("Major", Arrays.asList(2, 2, 1, 2, 2, 2, 1));
    Scale majorScale = new Scale(majorMode, tonic);

    // Printing scale notes for reference
    System.out.println("Scale notes: ");
    for (Note note : majorScale.notes) {
        System.out.print(note.name + note.octave + " ");
    }
    System.out.println();

    // Testing noteToTriad method with a note that is in the scale
    Note inScaleNote = new Note("E", 4);
    Chord inScaleChord = majorScale.noteToTriad(inScaleNote);
    System.out.println("Triad for in-scale note (" + inScaleNote.name + inScaleNote.octave + "): " + inScaleChord);

    // Testing noteToTriad method with a note that is not in the scale
    Note outOfScaleNote = new Note("G#", 4);
    Chord outOfScaleChord = majorScale.noteToTriad(outOfScaleNote);
    System.out.println("Triad for out-of-scale note (" + outOfScaleNote.name + outOfScaleNote.octave + "): " + outOfScaleChord);

    // Another test for out-of-scale note to observe randomness
    outOfScaleNote = new Note("A#", 4);
    outOfScaleChord = majorScale.noteToTriad(outOfScaleNote);
    System.out.println("Triad for out-of-scale note (" + outOfScaleNote.name + outOfScaleNote.octave + "): " + outOfScaleChord);

    // Testing getQuality method for different degrees in the scale
    for (int degree = 0; degree < majorScale.notes.size(); degree++) {
        Quality quality = majorScale.getQuality(degree);
        System.out.println("Quality for degree " + (degree + 1) + " (" + majorScale.notes.get(degree).name + "): " + quality.name);
    }
    }
}
