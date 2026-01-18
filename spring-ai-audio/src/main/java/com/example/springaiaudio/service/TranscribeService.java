package com.example.springaiaudio.service;

import org.springframework.ai.audio.transcription.AudioTranscriptionPrompt;
import org.springframework.ai.audio.transcription.AudioTranscriptionResponse;
import org.springframework.ai.openai.OpenAiAudioTranscriptionModel;
import org.springframework.ai.openai.OpenAiAudioTranscriptionOptions;
import org.springframework.ai.openai.api.OpenAiAudioApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FilenameFilter;
import java.util.HashMap;
import java.util.Map;

@Service
public class TranscribeService {
    @Value("${tts.audio-output-dir}")
    private String audioOutputDir;

    @Autowired
    private OpenAiAudioTranscriptionModel openAiAudioTranscriptionModel;

    public Map<String, String> transcribe() {
        File audioPath = new File(audioOutputDir);
        if (!audioPath.exists()) {
            return Map.of("message", "文件夹不存在:" + audioPath.getAbsolutePath());
        }
        File[] files = audioPath.listFiles();
        Map<String, String> result = new HashMap<>();
        for (File file : files) {
            OpenAiAudioTranscriptionOptions modelOptions = OpenAiAudioTranscriptionOptions.builder()
                    .responseFormat(OpenAiAudioApi.TranscriptResponseFormat.TEXT)
                    .language("zh")
                    .temperature(0f)
                    .build();
            AudioTranscriptionPrompt transcriptionPrompt = new AudioTranscriptionPrompt(
                    new FileSystemResource(file)
                    , modelOptions);
            AudioTranscriptionResponse response = openAiAudioTranscriptionModel.call(transcriptionPrompt);
            result.put(file.getAbsolutePath(), response.getResult().getOutput());
        }
        return result;
    }
}
