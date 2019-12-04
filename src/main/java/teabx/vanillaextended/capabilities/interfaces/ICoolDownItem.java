package teabx.vanillaextended.capabilities.interfaces;

public interface ICoolDownItem {
    boolean isOffCooldown();
    void updateCooldown();
    int getCooldown();
    int getMaxCooldown();
    void setCooldown(int cooldown);
}
