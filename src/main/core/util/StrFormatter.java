package main.core.util;

/**
 * String Formatter for replacing the placeholder {} with actual parameters
 * @author Joseph.Liu
 */
public class StrFormatter {

    public static String format(final String template, final Object... args){
        if(StrUtil.isBlank(template) || ArrayUtil.isEmpty(args)){
            return template;
        }
        final int templateLen = template.length();
        StringBuilder sb = new StringBuilder(templateLen + 50);
        int handledPosition = 0;
        int placeholderPosition;
        //iterate the args
        for(int i = 0; i < args.length; i++){
            //the first index of {}
            placeholderPosition = template.indexOf(StrUtil.EMPTY_JSON,handledPosition);
            // there is no {} in the left template string
            if(placeholderPosition == -1){
                //if there is no any {} in template from start
                if(handledPosition == 0){
                    return template;
                }
                sb.append(template,handledPosition,templateLen);
                return sb.toString();
            }
            //replace {} with args
            else{
                sb.append(template,handledPosition,placeholderPosition);
                sb.append(StrUtil.utf8ToStr(args[i]));
                handledPosition = placeholderPosition + 2;
            }
        }
        //append the left content of template to the position of last {} pair.
        sb.append(template,handledPosition,templateLen);
        return sb.toString();
    }
}
