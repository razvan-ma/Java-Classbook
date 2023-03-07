public interface Subject {
    void addObserver(Observer observer);
    void removeObserver(Observer observer);
    void notifyObservers(Grade grade);
    // Rămâne să stabiliti, voi ce clasă va implementa interfat,a Observer s, i ce clasă va implementa interfat,a Subject.
}

