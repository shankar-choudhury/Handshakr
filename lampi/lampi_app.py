from math import fabs

from kivy.app import App
from kivy.properties import NumericProperty, AliasProperty, BooleanProperty
from kivy.clock import Clock
from kivy.uix.popup import Popup
from kivy.uix.label import Label
import pigpio
import lampi.lampi_util
from .lamp_driver import LampDriver

TOLERANCE = 0.000001


class LampiApp(App):
    _hue = NumericProperty(1.0)
    _saturation = NumericProperty(1.0)
    _brightness = NumericProperty(1.0)
    lamp_is_on = BooleanProperty(True)

    def _get_hue(self):
        return self._hue

    def _set_hue(self, value):
        if fabs(self.hue - value) > TOLERANCE:
            self._hue = value

    def _get_saturation(self):
        return self._saturation

    def _set_saturation(self, value):
        if fabs(self.saturation - value) > TOLERANCE:
            self._saturation = value

    def _get_brightness(self):
        return self._brightness

    def _set_brightness(self, value):
        if fabs(self.brightness - value) > TOLERANCE:
            self._brightness = value

    hue = AliasProperty(_get_hue, _set_hue, bind=['_hue'])
    saturation = AliasProperty(_get_saturation, _set_saturation,
                               bind=['_saturation'])
    brightness = AliasProperty(_get_brightness, _set_brightness,
                               bind=['_brightness'])
    gpio17_pressed = BooleanProperty(False)

    def on_start(self):
        self.lamp_driver = LampDriver()
        Clock.schedule_once(lambda dt: self._update_leds(), 0.01)
        self.set_up_gpio_and_ip_popup()

    def on_hue(self, instance, value):
        Clock.schedule_once(lambda dt: self._update_leds(), 0.01)

    def on_saturation(self, instance, value):
        Clock.schedule_once(lambda dt: self._update_leds(), 0.01)

    def on_brightness(self, instance, value):
        Clock.schedule_once(lambda dt: self._update_leds(), 0.01)

    def on_lamp_is_on(self, instance, value):
        Clock.schedule_once(lambda dt: self._update_leds(), 0.01)

    def _update_leds(self):
        self.lamp_driver.set_lamp_state(self._hue, self._saturation,
                                        self._brightness, self.lamp_is_on)

    def set_up_gpio_and_ip_popup(self):
        """Set up a popup to display the Lampi's IP
        address when a button is pressed."""
        self.pi = pigpio.pi()
        self.pi.set_mode(17, pigpio.INPUT)
        self.pi.set_pull_up_down(17, pigpio.PUD_UP)
        Clock.schedule_interval(self._poll_gpio, 0.05)
        self.popup = Popup(title='IP Addresses',
                           content=Label(text='IP ADDRESS WILL GO HERE'),
                           size_hint=(1, 1), auto_dismiss=False)
        self.popup.bind(on_open=self.update_popup_ip_address)

    def update_popup_ip_address(self, instance):
        """Update the popup with the current IP address"""
        interface = "wlan0"
        ipaddr = lampi.lampi_util.get_ip_address(interface)
        instance.content.text = f"{interface}: {ipaddr}"

    def on_gpio17_pressed(self, instance, value):
        """Open or close the popup depending on the provided value"""
        if value:
            self.popup.open()
        else:
            self.popup.dismiss()

    def _poll_gpio(self, _delta_time):
        # GPIO17 is the rightmost button when looking front of LAMPI
        self.gpio17_pressed = not self.pi.read(17)
