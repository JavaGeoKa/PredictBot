package controller;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import bots.TelegramBot;

public class Controller {

	public static void main(String[] args) {
		System.out.println("Working Directory = " + System.getProperty("user.dir"));
		PrintWriter writer;
		try {
			writer = new PrintWriter("log.txt", "UTF-8");
			writer.println("Initialize");
			writer.close();

		} catch (FileNotFoundException | UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		ApiContextInitializer.init();
		TelegramBotsApi botsApi = new TelegramBotsApi();
		try {

			botsApi.registerBot(new TelegramBot());
			TelegramBot bot = new TelegramBot();

		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
	}

}
