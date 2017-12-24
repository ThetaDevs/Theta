package com.srgood.reasons.impl.commands.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.stream.IntStream;

import static com.srgood.reasons.impl.base.BaseConstants.GLOBAL_RANDOM;
import static com.srgood.reasons.impl.commands.CommandConstants.*;

public class ImageUtils {

    public static File renderVote(String voteName, String[] categoryNames, int[] categoryVotes) throws IOException {
        BufferedImage workImage = new BufferedImage(VOTE_IMAGE_WIDTH, VOTE_IMAGE_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        int colorInt = 0;
        int totalVotes = IntStream.of(categoryVotes).sum();

        Arrays.sort(categoryVotes);
        Arrays.sort(categoryNames);

        float angle = (float) totalVotes / (float) categoryVotes.length;
        int shadowOffset = -8;
        float oSpace = (((float) VOTE_IMAGE_HEIGHT - 320)) / (float) categoryNames.length;

        Graphics graphics = workImage.getGraphics();
        graphics.setFont(new Font("TimesRoman", Font.PLAIN, 35));
        FontMetrics metrics = graphics.getFontMetrics();

        //shading
        graphics.setColor(new Color(0, 0, 0, 50));
        graphics.fillArc(VOTE_IMAGE_WIDTH / 4 - 180 + shadowOffset, VOTE_IMAGE_HEIGHT / 2 - 180, 360, 360, 0, 360);
        graphics.drawString(voteName, VOTE_IMAGE_WIDTH / 2 + shadowOffset, VOTE_IMAGE_HEIGHT / 8);

        graphics.setColor(Color.WHITE);
        graphics.drawString(voteName, VOTE_IMAGE_WIDTH / 2, VOTE_IMAGE_HEIGHT / 8);

        for (int i : categoryVotes) {
            float percent = (float) i / (float) totalVotes;
            float angleM = percent * 360;
            graphics.setColor(COLORS[colorInt]);
            graphics.fillArc(VOTE_IMAGE_WIDTH / 4 - 175, VOTE_IMAGE_HEIGHT / 2 - 175, 350, 350, (int) angle, (int) angleM);
            angle += angleM;
            colorInt++;
        }
        int totalKeyHeight = (int) (oSpace * (float) categoryNames.length);

        for (int i = 0; i < categoryNames.length; i++) {

            graphics.setColor(new Color(0, 0, 0, 50));
            graphics.drawString(categoryNames[i] + " (" + categoryVotes[i] + ")", ((VOTE_IMAGE_WIDTH / 3) * 2) + 80 + (shadowOffset / 2), (int) (((VOTE_IMAGE_HEIGHT / 2) - (totalKeyHeight / 2)) + (i * oSpace) + metrics
                    .getHeight()) + (shadowOffset / 2));
            graphics.fillRect(((VOTE_IMAGE_WIDTH / 3) * 2) + 15 + (shadowOffset / 2), (int) (((VOTE_IMAGE_HEIGHT / 2) - (totalKeyHeight / 2)) + (i * oSpace) + 15) + (shadowOffset / 2), 30, 30);

            graphics.setColor(COLORS[i]);
            graphics.fillRect(((VOTE_IMAGE_WIDTH / 3) * 2) + 15, (int) (((VOTE_IMAGE_HEIGHT / 2) - (totalKeyHeight / 2)) + (i * oSpace) + 15), 30, 30);
            graphics.setColor(Color.WHITE);

            graphics.drawString(categoryNames[i] + " (" + categoryVotes[i] + ")", ((VOTE_IMAGE_WIDTH / 3) * 2) + 80, (int) (((VOTE_IMAGE_HEIGHT / 2) - (totalKeyHeight / 2)) + (i * oSpace) + metrics
                    .getHeight()));

        }

        graphics.dispose();
        workImage.flush();
        File file = File.createTempFile("" + GLOBAL_RANDOM.nextLong(), ".png");
        file.deleteOnExit();
        ImageIO.write(workImage, "png", file);
        return file;
    }

}
