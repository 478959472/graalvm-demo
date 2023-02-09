package org.example;



import com.tencent.SpeechClient;
import com.tencent.core.model.GlobalConfig;
import com.tencent.core.utils.ByteUtils;
import com.tencent.tts.model.*;
import com.tencent.tts.service.SpeechSynthesisListener;
import com.tencent.tts.service.SpeechSynthesizer;
import com.tencent.tts.utils.Ttsutils;

import javax.sound.sampled.*;
import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * 语音合成 example
 */

/**
 * Hello world!
 *
 */
public class App
{
    private static String codec = "pcm";
    private static int sampleRate = 16000;


    private static byte[] datas = new byte[0];

    /**
     * 语音合成
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException, InterruptedException {
        //从配置文件读取密钥
        Properties props = new Properties();
        props.load(new FileInputStream("../../config.properties"));
        String appId = props.getProperty("appId");
        String secretId =props.getProperty("secretId");
        String secretKey = props.getProperty("secretKey");
        //创建SpeechSynthesizerClient实例，目前是单例
        SpeechClient client = SpeechClient.newInstance(appId, secretId, secretKey);
        //初始化SpeechSynthesizerRequest，SpeechSynthesizerRequest包含请求参数
        SpeechSynthesisRequest request = SpeechSynthesisRequest.initialize();
        request.setCodec(codec);
        //request.setSampleRate(sampleRate);
        //request.setVolume(10);
        //request.setSpeed(2f);
        request.setVoiceType(101007);
        //使用客户端client创建语音合成实例
        SpeechSynthesizer speechSynthesizer = client.newSpeechSynthesizer(request, new MySpeechSynthesizerListener());
        //执行语音合成
        String ttsText = "通过 GraalVM native image，可以将 java 服务启动时间压缩数十倍，且生成的二进制文件大小也优于包括所有依赖的 jar 包。\n" +
                "\n" +
                "与传统 Java 运行模型相比，静态编译运行通过 AOT 避免了 JIT 的 CPU 开销，也避免了传统运行模型中一定存在的解释执行问题，使得程序性能较稳定。通过轻量化 SubstrateVM 实现，且也静态编译至 native image 中，提供了较快的 vm 性能和启动速度。\n" +
                "\n" +
                "但是，任何技术都有优缺点。而 Graalvm 静态编译则需要面临解决动态类加载、反射、动态代理等动态特性的适配问题。另外通过 native 运行的程序，将不再适用面向传统 JVM 程序的调试、监控、Agent 等功能。";
        LocalDateTime startTime = LocalDateTime.now();
        speechSynthesizer.synthesisLongText(ttsText);
        LocalDateTime localDateTime = LocalDateTime.now();
        long responseTime = Duration.between(startTime, localDateTime).toMillis();
        System.out.println("执行耗时:" + responseTime + "毫秒");
    }




    public static class MySpeechSynthesizerListener extends SpeechSynthesisListener {


        private AtomicInteger sessionId = new AtomicInteger(0);


        @Override
        public void onComplete(SpeechSynthesisResponse response) {
            System.out.println("onComplete");
            if (response.getSuccess()) {
                //根据具体的业务选择逻辑处理
                if ("mp3".equals(codec)) {
                    Ttsutils.saveResponseToFile(response.getAudio(),"./111.mp3");
                }
                if ("pcm".equals(codec)) {
                    //pcm 转 wav
                    String path = Ttsutils.responsePcm2Wav(sampleRate, response.getAudio(), response.getSessionId());
                    SourceDataLine sourceDataLine = null;
                    try {
                        AudioInputStream audioStream =AudioSystem.getAudioInputStream(new File(path));
                        AudioFormat audioFormat = audioStream.getFormat();
                        DataLine.Info info = new DataLine.Info(Clip.class, audioFormat);
                        sourceDataLine = (SourceDataLine) AudioSystem.getLine(info);
                        sourceDataLine.open(audioFormat);
                    } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                        e.printStackTrace();
                    }
                    if (sourceDataLine != null) {
                        sourceDataLine.start();
                    }
                }
                if ("opus".equals(codec)) {
                    //opus
                    System.out.println("OPUS:" + response.getSessionId() + " length:" + response.getAudio().length);
                }
            }
            System.out.println("结束：" + response.getSuccess() + " " + response.getCode()
                    + " " + response.getMessage() + " " + response.getEnd());
        }


        //语音合成的语音二进制数据
        @Override
        public void onMessage(byte[] data) {
            //System.out.println("onMessage:" + data.length);
            // Your own logic.
            System.out.println("onMessage length:" + data.length);


            sessionId.incrementAndGet();
        }


        @Override
        public void onFail(SpeechSynthesisResponse response) {
            System.out.println("onFail");
        }
    }

}
