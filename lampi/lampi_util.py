import socket
import fcntl
import struct


def get_ip_address(ifname: str) -> str:
    """Get the IP address associated with the given network interface.

    Parameters
    ----------
    ifname : str
        The network interface to check (e.g. "wlan0")

    Returns
    -------
    str
        The IP address of this machine on the given interface
    """
    s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    try:
        return socket.inet_ntoa(fcntl.ioctl(
            s.fileno(),
            0x8915,  # SIOCGIFADDR
            struct.pack('256s', bytes(ifname[:15], 'utf-8'))
        )[20:24])
    except Exception:
        return ''
