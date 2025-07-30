import 'package:flutter/material.dart';
import 'screens.dart';

class OpeningAnimation extends StatefulWidget {
  const OpeningAnimation({super.key});

  @override
  State<OpeningAnimation> createState() => _OpeningAnimationState();
}

class _OpeningAnimationState extends State<OpeningAnimation>
  with TickerProviderStateMixin {
  late AnimationController _mainController;
  late AnimationController _fadeController;
  
  late Animation<double> _billRiseAnimation;
  late Animation<double> _billScaleAnimation;
  late Animation<double> _splitAnimation;
  late Animation<double> _phoneAnimation;
  late Animation<double> _billDisappearAnimation;
  late Animation<double> _phoneDisappearAnimation;
  late Animation<double> _backgroundSlideAnimation;

  @override
  void initState() {
    super.initState();
    
    // Controller chính cho animation tờ tiền
    _mainController = AnimationController(
      duration: const Duration(milliseconds: 3500),
      vsync: this,
    );
    
    // Controller cho fade nền
    _fadeController = AnimationController(
      duration: const Duration(milliseconds: 600),
      vsync: this,
    );

    // Animation tờ tiền bay lên từ dưới đến giữa màn hình
    _billRiseAnimation = Tween<double>(
      begin: 1.2,
      end: 0.4,
    ).animate(CurvedAnimation(
      parent: _mainController,
      curve: const Interval(0.0, 0.3, curve: Curves.easeOutCubic),
    ));

    // Animation scale tờ tiền khi ở giữa
    _billScaleAnimation = Tween<double>(
      begin: 1.0,
      end: 0.8,
    ).animate(CurvedAnimation(
      parent: _mainController,
      curve: const Interval(0.25, 0.3, curve: Curves.easeInOut),
    ));

    // Animation 4 tờ tiền nhỏ bay ra 4 góc
    _splitAnimation = Tween<double>(
      begin: 0.0,
      end: 1.0,
    ).animate(CurvedAnimation(
      parent: _mainController,
      curve: const Interval(0.3, 0.65, curve: Curves.easeOutCubic),
    ));

    // Animation điện thoại xuất hiện
    _phoneAnimation = Tween<double>(
      begin: 0.0,
      end: 1.0,
    ).animate(CurvedAnimation(
      parent: _mainController,
      curve: const Interval(0.35, 0.55, curve: Curves.easeOutBack),
    ));

    // Animation tờ tiền con mờ dần khi bay đến điện thoại
    _billDisappearAnimation = Tween<double>(
      begin: 1.0,
      end: 0.0,
    ).animate(CurvedAnimation(
      parent: _mainController,
      curve: const Interval(0.5, 0.5, curve: Curves.easeInCubic),
    ));

    // Animation điện thoại biến mất
    _phoneDisappearAnimation = Tween<double>(
      begin: 1.0,
      end: 0.0,
    ).animate(CurvedAnimation(
      parent: _mainController,
      curve: const Interval(0.7, 0.8, curve: Curves.easeIn),
    ));

    // Animation nền xanh mờ dần
    _backgroundSlideAnimation = Tween<double>(
      begin: 1.0,
      end: 0.0,
    ).animate(CurvedAnimation(
      parent: _fadeController,
      curve: Curves.easeOut,
    ));

    // Bắt đầu animation
    _startAnimation();
  }

  void _startAnimation() async {
    await Future.delayed(const Duration(milliseconds: 300));
    await _mainController.forward();
    await Future.delayed(const Duration(milliseconds: 200));
    await _fadeController.forward();
    
    // Chuyển sang WelcomePage
    if (mounted) {
      Navigator.of(context).pushReplacement(
        PageRouteBuilder(
          pageBuilder: (context, animation, secondaryAnimation) =>
              const WelcomePage(),
          transitionDuration: const Duration(milliseconds: 600),
          transitionsBuilder: (context, animation, secondaryAnimation, child) {
            return FadeTransition(opacity: animation, child: child);
          },
        ),
      );
    }
  }

  @override
  void dispose() {
    _mainController.dispose();
    _fadeController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    final screenWidth = MediaQuery.of(context).size.width;
    final screenHeight = MediaQuery.of(context).size.height;

    return Scaffold(
      body: AnimatedBuilder(
        animation: Listenable.merge([_mainController, _fadeController]),
        builder: (context, child) {
          return Container(
            width: double.infinity,
            height: double.infinity,
            child: Stack(
              children: [
                // WelcomePage hiển thị phía dưới nền xanh
                Positioned.fill(
                  child: const WelcomePage(),
                ),
                
                // Nền xanh mờ dần
                Container(
                  width: double.infinity,
                  height: double.infinity,
                  decoration: BoxDecoration(
                    gradient: LinearGradient(
                      begin: Alignment.topCenter,
                      end: Alignment.bottomCenter,
                      colors: [
                        const Color(0xFF0041C4).withOpacity(_backgroundSlideAnimation.value),
                        const Color(0xFF0066FF).withOpacity(_backgroundSlideAnimation.value),
                      ],
                    ),
                  ),
                ),

                // Tờ tiền chính bay lên
                if (_splitAnimation.value < 0.3) ...[
                  Positioned(
                    left: screenWidth * 0.5 - 30,
                    top: screenHeight * _billRiseAnimation.value,
                    child: Transform.scale(
                      scale: _billScaleAnimation.value,
                      child: Opacity(
                        opacity: 1.0 - (_splitAnimation.value * 3.33), 
                        child: Container(
                          width: 60,
                          height: 120,
                          decoration: BoxDecoration(
                            color: Colors.green[400],
                            borderRadius: BorderRadius.circular(8),
                            boxShadow: [
                              BoxShadow(
                                color: Colors.black.withOpacity(0.3),
                                blurRadius: 10,
                                offset: const Offset(0, 5),
                              ),
                            ],
                          ),
                          child: Column(
                            mainAxisAlignment: MainAxisAlignment.center,
                            children: [
                              Text(
                                '\$',
                                style: TextStyle(
                                  color: Colors.white,
                                  fontSize: 24,
                                  fontWeight: FontWeight.bold,
                                ),
                              ),
                              Text(
                                '100',
                                style: TextStyle(
                                  color: Colors.white,
                                  fontSize: 16,
                                  fontWeight: FontWeight.w600,
                                ),
                              ),
                            ],
                          ),
                        ),
                      ),
                    ),
                  ),
                ],

                // 4 tờ tiền nhỏ bay ra 4 góc
                if (_splitAnimation.value > 0.0) ...[
                  // Tờ tiền góc trên trái
                  _buildSmallBill(
                    screenWidth * (0.5 - 0.4 * _splitAnimation.value),
                    screenHeight * (0.4 - 0.25 * _splitAnimation.value),
                    -45 * _splitAnimation.value,
                    _splitAnimation.value,
                  ),
                  // Tờ tiền góc trên phải
                  _buildSmallBill(
                    screenWidth * (0.5 + 0.4 * _splitAnimation.value),
                    screenHeight * (0.4 - 0.25 * _splitAnimation.value),
                    45 * _splitAnimation.value,
                    _splitAnimation.value,
                  ),
                  // Tờ tiền góc dưới trái
                  _buildSmallBill(
                    screenWidth * (0.5 - 0.4 * _splitAnimation.value),
                    screenHeight * (0.4 + 0.45 * _splitAnimation.value),
                    -135 * _splitAnimation.value,
                    _splitAnimation.value,
                  ),
                  // Tờ tiền góc dưới phải
                  _buildSmallBill(
                    screenWidth * (0.5 + 0.4 * _splitAnimation.value),
                    screenHeight * (0.4 + 0.45 * _splitAnimation.value),
                    135 * _splitAnimation.value,
                    _splitAnimation.value,
                  ),
                ],

                // 4 điện thoại ở 4 góc
                if (_phoneAnimation.value > 0.0) ...[
                  // Điện thoại góc trên trái
                  _buildPhone(
                    screenWidth * 0.1,
                    screenHeight * 0.12,
                    _phoneAnimation.value,
                  ),
                  // Điện thoại góc trên phải
                  _buildPhone(
                    screenWidth * 0.9 - 50,
                    screenHeight * 0.12,
                    _phoneAnimation.value,
                  ),
                  // Điện thoại góc dưới trái
                  _buildPhone(
                    screenWidth * 0.1,
                    screenHeight * 0.88 - 80,
                    _phoneAnimation.value,
                  ),
                  // Điện thoại góc dưới phải
                  _buildPhone(
                    screenWidth * 0.9 - 50,
                    screenHeight * 0.88 - 80,
                    _phoneAnimation.value,
                  ),
                ],
              ],
            ),
          );
        },
      ),
    );
  }

  Widget _buildSmallBill(double x, double y, double rotation, double opacity) {
    // Tính toán opacity: tờ tiền vừa bay vừa mờ dần đến khi chạm điện thoại
    // Khi _splitAnimation từ 0->1, tờ tiền sẽ mờ từ 1->0 dần đều
    double flyOpacity = opacity;
    double fadeOpacity = _billDisappearAnimation.value;
    double finalOpacity = flyOpacity * fadeOpacity;
    
    return Positioned(
      left: x - 20,
      top: y - 30,
      child: Transform.rotate(
        angle: rotation * 3.14159 / 180,
        child: Opacity(
          opacity: finalOpacity,
          child: Container(
            width: 40,
            height: 60,
            decoration: BoxDecoration(
              color: Colors.green[400],
              borderRadius: BorderRadius.circular(6),
              boxShadow: [
                BoxShadow(
                  color: Colors.black.withOpacity(0.2 * finalOpacity),
                  blurRadius: 5,
                  offset: const Offset(0, 3),
                ),
              ],
            ),
            child: Column(
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                Text(
                  '\$',
                  style: TextStyle(
                    color: Colors.white,
                    fontSize: 14,
                    fontWeight: FontWeight.bold,
                  ),
                ),
                Text(
                  '25',
                  style: TextStyle(
                    color: Colors.white,
                    fontSize: 10,
                    fontWeight: FontWeight.w600,
                  ),
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }

  Widget _buildPhone(double x, double y, double scale) {
    // Điện thoại biến mất sau khi tờ tiền đã vào
    double finalOpacity = _phoneDisappearAnimation.value;

    Transform.scale(
      scale: scale + (1 - _phoneDisappearAnimation.value) * 0.05,
    );
    
    return Positioned(
      left: x,
      top: y,
      child: Transform.scale(
        scale: scale,
        child: Opacity(
          opacity: finalOpacity,
          child: Container(
            width: 50,
            height: 80,
            decoration: BoxDecoration(
              color: Color.fromARGB(255, 29, 29, 29),
              borderRadius: BorderRadius.circular(12),
            ),
            child: Column(
              children: [
                Container(
                  margin: const EdgeInsets.all(4),
                  height: 60,
                  decoration: BoxDecoration(
                    color: Color.fromARGB(255, 249, 252, 252),
                    borderRadius: BorderRadius.circular(8),
                  ),
                  child: Center(
                    child: Icon(
                      Icons.phone_android,
                      color: Colors.white,
                      size: 20,
                    ),
                  ),
                ),
                Container(
                  width: 20,
                  height: 4,
                  decoration: BoxDecoration(
                    color: Colors.grey[400],
                    borderRadius: BorderRadius.circular(2),
                  ),
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }
}