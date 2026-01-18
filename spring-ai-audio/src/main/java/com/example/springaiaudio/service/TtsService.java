package com.example.springaiaudio.service;

import org.springframework.ai.audio.tts.TextToSpeechPrompt;
import org.springframework.ai.audio.tts.TextToSpeechResponse;
import org.springframework.ai.openai.OpenAiAudioSpeechModel;
import org.springframework.ai.openai.OpenAiAudioSpeechOptions;
import org.springframework.ai.openai.api.OpenAiAudioApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;

@Service
public class TtsService {

    @Autowired
    private OpenAiAudioSpeechModel speechModel;

    @Value("${tts.audio-output-dir}")
    private String audioOutputDir;

    public String textToAudio(String text) throws Exception {
        //准备生成语音的配置项
        OpenAiAudioSpeechOptions openAiAudioSpeechOptions = OpenAiAudioSpeechOptions.builder()
                .voice(OpenAiAudioApi.SpeechRequest.Voice.ALLOY)
                .responseFormat(OpenAiAudioApi.SpeechRequest.AudioResponseFormat.MP3)
                .speed(1.0)
                .build();
        //准备prompt
        TextToSpeechPrompt prompt = new TextToSpeechPrompt(text, openAiAudioSpeechOptions);
        //生成语音
        TextToSpeechResponse textToSpeechResponse = speechModel.call(prompt);
        System.out.println("textToSpeechResponse = " + textToSpeechResponse);

        //从反馈中回去音频并保存到文件中
        byte[] audio = textToSpeechResponse.getResult().getOutput();
        File dir = new File(audioOutputDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File audioFile = new File(dir, "tts-" + System.currentTimeMillis() + ".mp3");
        FileOutputStream fileOutputStream = new FileOutputStream(audioFile);
        fileOutputStream.write(audio);
        return audioFile.getAbsolutePath();
    }
}
