package com.example.bemyfriend;

public class Hobbies {
    private boolean boardGames, science,nature,sports,art,gaming,music;
    private String other;

    public Hobbies(boolean boardGames, boolean science, boolean nature, boolean sports, boolean art,
                   boolean gaming, boolean music, String other) {
        this.boardGames = boardGames;
        this.science = science;
        this.nature = nature;
        this.sports = sports;
        this.art = art;
        this.gaming = gaming;
        this.music = music;
        this.other = other;
    }

    public Hobbies() {
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    public void setBoardGames(boolean boardGames) {
        this.boardGames = boardGames;
    }

    public void setScience(boolean science) {
        this.science = science;
    }

    public void setNature(boolean nature) {
        this.nature = nature;
    }

    public void setSports(boolean sports) {
        this.sports = sports;
    }

    public void setArt(boolean art) {
        this.art = art;
    }

    public void setGaming(boolean gaming) {
        this.gaming = gaming;
    }

    public void setMusic(boolean music) {
        this.music = music;
    }

    public boolean getBoardGames() {
        return boardGames;
    }

    public boolean getScience() {
        return science;
    }

    public boolean getNature() {
        return nature;
    }

    public boolean getSports() {
        return sports;
    }

    public boolean getArt() {
        return art;
    }

    public boolean getGaming() {
        return gaming;
    }

    public boolean getMusic() {
        return music;
    }
}
