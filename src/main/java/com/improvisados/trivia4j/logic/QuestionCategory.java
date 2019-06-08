/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.improvisados.trivia4j.logic;

/**
 *
 * @author joaquin
 */
public enum QuestionCategory
{
    General_Knowledge(9),
    Entertainment_Books(10),
    Entertainment_Film(11),
    Entertainment_Music(12),
    Entertainment_Musicals_and_Theatres(13),
    Entertainment_Television(14),
    Entertainment_Video_Games(15),
    Entertainment_Board_Games(16),
    Science_and_Nature(17),
    Science_Computers(18),
    Science_Mathematics(19),
    Mythology(20),
    Sports(21),
    Geography(22),
    History(23),
    Politics(24),
    Art(25),
    Celebrities(26),
    Animals(27),
    Vehicles(28),
    Entertainment_Comics(29),
    Science_Gadgets(30),
    Entertainment_Japanese_Anime_and_Manga(31),
    Entertainment_Cartoon_and_Animations(32);

    private int value;

    private QuestionCategory(int val)
    {
        this.value = val;
    }

    public int getValue()
    {
        return value;
    }
        
    public static QuestionCategory getValue(int value) {
  for(QuestionCategory e: QuestionCategory.values()) {
    if(e.value == value) {
      return e;
    }
  }
  return null;// not found
}

    
    
}
