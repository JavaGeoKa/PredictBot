package bots;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.vdurmont.emoji.EmojiParser;

import imagga.ImgPredictor;
import model.JsonParser;
import repoimgur.ImgurUploader;

public class TelegramBot extends TelegramLongPollingBot {
	private String dog_emoji = EmojiParser.parseToUnicode(":dog: info");
	private long chat_id;
//	private static Long chatId = null;

	public void setButtons(SendMessage sendMessage) {
		ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
		sendMessage.setReplyMarkup(replyKeyboardMarkup);
		replyKeyboardMarkup.setSelective(true);
		replyKeyboardMarkup.setResizeKeyboard(true);
		replyKeyboardMarkup.setOneTimeKeyboard(false);

		List<KeyboardRow> keyboard = new ArrayList<KeyboardRow>();
		KeyboardRow keyboardFirstRow = new KeyboardRow();
		keyboardFirstRow.add(dog_emoji);
		keyboard.add(keyboardFirstRow);
		replyKeyboardMarkup.setKeyboard(keyboard);
	}

	public void onUpdateReceived(Update update) {
		chat_id = update.getMessage().getChatId();
		SendMessage message = new SendMessage();
		message.setChatId(chat_id);
		message.setText("@picturepredictbot");

//		 TEXT AREA
		if (update.hasMessage() && update.getMessage().hasText()) {
			if (update.getMessage().getText().equals("/start")) {

//				=======LOG AREA
				String user_first_name = update.getMessage().getChat().getFirstName();
				String user_last_name = update.getMessage().getChat().getLastName();
				String user_username = update.getMessage().getChat().getUserName();
				long user_id = update.getMessage().getChat().getId();
				String message_input_text = update.getMessage().getText();
				long chat_id = update.getMessage().getChatId();

				log(user_first_name, user_last_name, user_username, user_id, message_input_text, chat_id, "", "");
				// ==========================
				String message_text = "Download picture please " + dog_emoji;
				message.setText(message_text);
			} else if (update.getMessage().getText().equals(dog_emoji)) {
				message.setText("enjoy! i can recognize your photo. send me photo please");
			} else {
				message.setText("Order is not recognized");
			}
		}

		// PICTURE AREA
		if (update.hasMessage() && update.getMessage().hasPhoto()) {

			String user_first_name = update.getMessage().getChat().getFirstName();
			String user_last_name = update.getMessage().getChat().getLastName();
			String user_username = update.getMessage().getChat().getUserName();
			long user_id = update.getMessage().getChat().getId();
			String text = update.getMessage().getText();
			long chat_id = update.getMessage().getChatId();

			Message photoMessage = update.getMessage();
			long chatId = photoMessage.getChatId();
			List<PhotoSize> photos = update.getMessage().getPhoto();
			System.out.println(photos.size() + "photos size");

			PhotoSize p = photos.stream().max(Comparator.comparing(PhotoSize::getFileSize)).orElse(null);

			try {
//				ImageSaver.save(downloadPhotoByFilePath(getFilePath(p)));

				// get str base64 from resieved file
				String stringBase64 = ImgurUploader.toBase64(downloadPhotoByFilePath(getFilePath(p)));

				// upload str to imgur
				String jsonInImgur = ImgurUploader.getImgurContent("16b4bf413fefd65", stringBase64);
				System.out.println("json in Imgur ->" + jsonInImgur);

				// get link from Imgur

				String stringLink = JsonParser.jsonParse(jsonInImgur);
				System.out.println(stringLink);

				// Imagga predict by link
				String predicts = ImgPredictor.predictByLink(stringLink);

				// return predicts
				System.out.println("logging..");
//				log(user_first_name, user_last_name, user_username, user_id, text, chat_id, stringLink, predicts);
				say(chatId, stringLink, predicts);

			} catch (IOException e) {
				System.out.println("----------------------------SaverException");
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("----------------------------UploaderException");

			}

		}

		setButtons(message);
		try {
			execute(message);
		} catch (TelegramApiException e) {
			e.printStackTrace();
			System.out.println("===========================executeException");
		}
	}

	public java.io.File downloadPhotoByFilePath(String filePath) {
		try {
			// Download the file calling AbsSender::downloadFile method
			return downloadFile(filePath);
		} catch (TelegramApiException e) {
//			e.printStackTrace();
			System.out.println("============================downloadFileException");
		}

		return null;
	}

	public String getFilePath(PhotoSize photo) {
		Objects.requireNonNull(photo);

		if (photo.hasFilePath()) { // If the file_path is already present, we are done!
			return photo.getFilePath();
		} else { // If not, let find it
			// We create a GetFile method and set the file_id from the photo
			GetFile getFileMethod = new GetFile();
			getFileMethod.setFileId(photo.getFileId());
			try {
				// We execute the method using AbsSender::execute method.
				File file = execute(getFileMethod);
				// We now have the file_path
				return file.getFilePath();
			} catch (TelegramApiException e) {
//				e.printStackTrace();
				System.out.println("=======================getFilePathException");
			}
		}

		return null; // Just in case
	}

	public void say(Long chatId, String url, String predicts) {
		SendMessage message = new SendMessage();

		message.enableMarkdown(true);
		message.setChatId(chatId);
		System.out.println(url + "\n" + predicts);
		message.setText(url + "\n" + predicts);
		try {
			execute(message);
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}

	}

	private void log(String first_name, String last_name, String username, long user_id, String txt, Long chat_id,
			String stringLink, String predicts) {
		if (first_name == null)
			first_name = "anonimous";
		if (last_name == null)
			last_name = "anonimous";
		if (username == null)
			username = "anonimous userName";
		if (user_id == 0L)
			user_id = 0;

		if (txt == null)
			txt = "empty txt";

		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		String log = "Date " + date + " , Message from " + first_name + " " + last_name + " , username " + username
				+ ", id = " + user_id + ", " + "chat_id = " + chat_id + ", Imgur url = " + stringLink + ", predicts = "
				+ predicts + "\n";

		try {
			saveLog(log);
		} catch (IOException e) {
			System.out.println("-------------------SaveLogException");
			e.printStackTrace();
		}

	}

	public static void saveLog(String log) throws IOException {
		Path path = Paths.get(System.getProperty("user.dir") + "/log.txt");
		Files.write(path, log.getBytes(), StandardOpenOption.APPEND);
	}

	public String getBotUsername() {
		return "ImagePredictBot";
	}

	public String getBotToken() {
		return "1291842808:AAHvZuZd7ATwDfT6VCX7t4FJIilFTbiT_cg";
	}

}
