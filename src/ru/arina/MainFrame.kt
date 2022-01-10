package ru.arina



import ru.arina.graphics.ExplFunct
import ru.arina.graphics.ParamFunct
import java.awt.Color
import java.awt.Dimension
import java.awt.event.*
import java.beans.PropertyChangeListener
import javax.swing.*
import kotlin.math.abs
import kotlin.reflect.jvm.internal.impl.util.Check

class MainFrame : JFrame(){
    private val minDim = Dimension(600, 700)
    private val mainPanel: GraphicsPanel
    private val controlPanel: JPanel


    private val xMinM: SpinnerNumberModel
    private val yMinM: SpinnerNumberModel
    private val xMaxM: SpinnerNumberModel
    private val yMaxM: SpinnerNumberModel
    private val xMin: JSpinner
    private val yMin:JSpinner
    private val xMax: JSpinner
    private val yMax:JSpinner

    private val tMinM: SpinnerNumberModel
    private val tMaxM: SpinnerNumberModel
    private val tMin:JSpinner
    private val tMax: JSpinner

    private val textXMin: JLabel
    private  val textXMax: JLabel
    private val textYMin: JLabel
    private val textYMax: JLabel

    private  val textTMax: JLabel
    private val textTMin: JLabel

  /*  private val chButton1 = JButton("Цвет графика")
    private val chButton2 = JButton("Цвет точек")
    private val chButton3 = JButton("Цвет производной")*/
    init {
        minimumSize = minDim
        defaultCloseOperation = EXIT_ON_CLOSE
        xMinM = SpinnerNumberModel(-5.0, -100.0, 4.9, 0.1)
        xMin = JSpinner(xMinM)
        xMaxM = SpinnerNumberModel(5.0, -4.9, 100.0, 0.1)
        xMax = JSpinner(xMaxM)
        yMinM = SpinnerNumberModel(-5.0, -100.0, 4.9, 0.1)
        yMin = JSpinner(yMinM)
        yMaxM = SpinnerNumberModel(5.0, -4.9, 100.0, 0.1)
        yMax = JSpinner(yMaxM)

      tMinM = SpinnerNumberModel(-5.0, -100.0, 4.9, 0.1)
      tMin = JSpinner(tMinM)
      tMaxM = SpinnerNumberModel(5.0, -4.9, 100.0, 0.1)
      tMax = JSpinner(tMaxM)

        val mainPlane = CartesianPlane(
            xMinM.value as Double,
            xMaxM.value as Double,
            yMinM.value as Double,
            yMaxM.value as Double
        )
       /* val tMin:Double = -5.0
        val tMax:Double = 5.0*/
        val cartesianPainter = CartesianPainter(mainPlane)//отрисовка системы координат
        val fexpl = {x:Double ->  8 / (4+x*x) }
        val explFunct = ExplFunct(mainPlane, fexpl)
        val x = {t:Double -> 1-t}
        val y = {t:Double -> 1-t*t}
        val paramFunct = ParamFunct(mainPlane, x, y,/*mainPlane.tMin,mainPlane.tMax*/ tMin.value as Double,tMax.value as Double)
        /*val pointPainter= PointPainter(mainPlane)//отрисовка точек
        val functionPainter=FunctionPainter(mainPlane)//отрисовка полинома
        val derPainter=FunctionPainter(mainPlane)
        //var polynomial = Newton(mutableMapOf())
        lateinit var polynomial:Newton
        //functionPainter.addPolynomial(polynomial)*/



        val painters = mutableListOf(cartesianPainter, explFunct, paramFunct)
        mainPanel = GraphicsPanel(painters).apply {
            background = Color.WHITE
        }
       /* checkboxPoint= JCheckBox()
        checkboxGraphics= JCheckBox()
        checkboxDerivative= JCheckBox()
        checkboxGraphics.isSelected=true
        checkboxPoint.isSelected= true
        checkboxDerivative.isSelected= true
        var k=0*/
        textXMin= JLabel().apply {
            text="Xmin"
        }
        textXMax=JLabel().apply {
            text="Xmax"
        }
        textYMin= JLabel().apply {
            text="Ymin"
        }
        textYMax=JLabel().apply {
            text="Ymax"
        }

      textTMin= JLabel().apply {
          text="Tmin"
      }
      textTMax=JLabel().apply {
          text="Tmax"
      }

     /*   textPoint= JLabel().apply {
            text="Отобразить точки"
        }
        textGraphic= JLabel().apply {
            text="Отобразить график функции"
        }
        textDerivative= JLabel().apply {
            text="Отобразить производную"
        }
        colorpanelPoint=JPanel().apply{
            background = pointPainter.pointColor
        }
        colorpanelGraphic=JPanel().apply{
            background = functionPainter.funColor
        }
*/
/*
        colorpanelDerivative= JPanel().apply {
            background=derPainter.funColor
        }
        mainPanel.addMouseListener(object : MouseAdapter(){
            override fun mouseClicked(e: MouseEvent?)
            {
                if(e?.point != null){
                    if(e.button == 1) {
                        if(k == 0){
                            polynomial = Newton(mutableMapOf(mainPlane.xScr2Crt(e.point.x) to mainPlane.yScr2Crt(e.point.y)))//добавляем узел кполиному Ньютона
                            pointPainter.points.put(mainPlane.xScr2Crt(e.point.x), mainPlane.yScr2Crt(e.point.y))//добавляем точку на отрисовку
                            functionPainter.polynom = polynomial::invoke
                            k = 1
                        }
                        else{
                            var p = true
                            for(i in 0 until pointPainter.points.size){//если попадаем в уже нарисованную точку, то новую не рисуем
                                if((e.point.x < mainPlane.xCrt2Scr(pointPainter.points.keys.elementAt(i))+20
                                            && e.point.x > mainPlane.xCrt2Scr(pointPainter.points.keys.elementAt(i))-20)) {
                                    p = false
                                    break
                                }
                            }
                            if(p){
                                //добавляем новую точку и перерисовываем полином
                                polynomial.add(mutableMapOf(mainPlane.xScr2Crt(e.point.x) to mainPlane.yScr2Crt(e.point.y)))
                                pointPainter.points.put(mainPlane.xScr2Crt(e.point.x), mainPlane.yScr2Crt(e.point.y))
                                var poll:Polynom = Polynom(polynomial.diff())
                                derPainter.polynom = poll::invoke
                            }
                        }
                        mainPanel.repaint()
                    }
                    //если зажата правая кнопка мыши, то удаляем точку
                    if(e.button == 3){
                        if(painters.size != 1){
                            for(i in 0 until pointPainter.points.size){
                                //если попадаем в какую-нибудь точку, то удаляем ее
                                if((mainPlane.xScr2Crt(e.point.x)+0.1 >pointPainter.points.keys.elementAt(i) && mainPlane.xScr2Crt(e.point.x)-0.1 < pointPainter.points.keys.elementAt(i))
                                    && (mainPlane.yScr2Crt(e.point.y)+0.1 >pointPainter.points.values.elementAt(i) && mainPlane.yScr2Crt(e.point.y)-0.1 < pointPainter.points.values.elementAt(i))){
                                    if(pointPainter.points.size == 1){
                                        //удаляем все из отрисовщиков
                                        painters.remove(pointPainter)
                                        painters.remove(functionPainter)
                                        painters.remove(derPainter)
                                    }
                                    pointPainter.points.remove(pointPainter.points.keys.elementAt(i))
                                    break
                                }
                            }
                            //если удалили не все точки
                            if(pointPainter.points.size != 0){
                                //отрисовываем полином заново уже без той точки
                                var newp = Newton(pointPainter.points)
                                polynomial = newp
                                var poll:Polynom = Polynom(polynomial.diff())
                                derPainter.polynom = poll::invoke
                                functionPainter.polynom = polynomial::invoke
                            }
                            //если мы удалили вообще все точки
                            if(pointPainter.points.size == 0){
                                polynomial.ind.clear()
                                polynomial.coeff = Polynom().coeff
                                var poll:Polynom = Polynom()
                                derPainter.polynom = poll::invoke
                                functionPainter.polynom = polynomial::invoke
                                k = 0
                            }
                        }
                        mainPanel.repaint()
                    }
                }
            }
        })

        fun deletePol(){
            if(k == 1) painters.remove(functionPainter)
            mainPanel.repaint()
        }

        fun ShowPolynom(){
            if(k == 1){
                painters.add(0, functionPainter)
            }
            mainPanel.repaint()
        }
        fun deletePoints(){
            if(k == 1) painters.remove(pointPainter)
            mainPanel.repaint()
        }
        fun showPoints(){
            if(k == 1) painters.add(pointPainter)
            mainPanel.repaint()
        }

        fun deletePolDiff(){
            if(k == 1){
                painters.remove(derPainter)
            }
            mainPanel.repaint()
        }
        fun ShowPolynomDiff(){
            if(k == 1) painters.addAll(mutableListOf(derPainter))
            mainPanel.repaint()
        }
        checkboxDerivative.addMouseListener(object : MouseAdapter(){
            override fun mouseClicked(e: MouseEvent?) {
                if(checkboxDerivative.isSelected){
                    ShowPolynomDiff()
                    checkboxDerivative.isSelected = true
                }
                else{
                    deletePolDiff()
                    checkboxDerivative.isSelected = false
                }
            }
        })

        colorpanelPoint.addMouseListener(object : MouseAdapter() {
            override fun mouseClicked(e: MouseEvent?) {
                val background1 = JColorChooser.showDialog(null, "Select a color", Color.RED)
                colorpanelPoint.background=background1
                pointPainter.pointColor=background1
                mainPanel.repaint()
            }
        })
        colorpanelGraphic.addMouseListener(object : MouseAdapter() {
            override fun mouseClicked(e: MouseEvent?) {
                val background = JColorChooser.showDialog(null, "Select a color", Color.RED)
                colorpanelGraphic.background=background
                functionPainter.funColor=background
                mainPanel.repaint()
            }
        })
        colorpanelDerivative.addMouseListener(object : MouseAdapter() {
            override fun mouseClicked(e: MouseEvent?) {
                val background2 = JColorChooser.showDialog(null, "Select a color", Color.RED)
                colorpanelDerivative.background=background2
                derPainter.funColor=background2
                mainPanel.repaint()
            }
        })

        checkboxGraphics.addMouseListener(object : MouseAdapter(){
            override fun mouseClicked(e: MouseEvent?) {
                if(checkboxGraphics.isSelected){
                    if(checkboxPoint.isSelected) {
                        deletePoints()
                        ShowPolynom()
                        showPoints()
                        checkboxGraphics.isSelected = true
                    }
                    else{
                        ShowPolynom()
                        checkboxGraphics.isSelected = true
                    }
                }
                else{
                    deletePol()
                    checkboxGraphics.isSelected = false
                }
            }
        })
        checkboxPoint.addMouseListener(object : MouseAdapter(){
            override fun mouseClicked(e: MouseEvent?) {
                if(checkboxPoint.isSelected){
                    showPoints()
                    checkboxPoint.isSelected = true
                }
                else{
                    deletePoints()
                    checkboxPoint.isSelected = false
                }
            }
        })*/

        mainPlane.pixelSize = mainPanel.size
        mainPanel.addComponentListener(object: ComponentAdapter() {
            override fun componentResized(e: ComponentEvent?) {
                mainPlane.pixelSize = mainPanel.size
                mainPanel.repaint()
            }
        })


        controlPanel= JPanel()
        layout = GroupLayout(contentPane).apply{
            setHorizontalGroup(
                createSequentialGroup()
                    .addGap(4)
                    .addGroup(
                        createParallelGroup()
                            .addComponent(mainPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
                            .addComponent(controlPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
                    )
                    .addGap(4)
            )

            setVerticalGroup(
                createSequentialGroup()
                    .addGap(4)
                    .addComponent(mainPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
                    .addGap(4)
                    .addComponent(controlPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addGap(4)
            )
        }
        controlPanel.layout=GroupLayout(controlPanel).apply{ //делаем контрольную панель
            setHorizontalGroup(createSequentialGroup()
                .addGap(8)

                .addGroup(
                    createParallelGroup()
                        .addComponent(textXMin, GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE)
                        .addComponent(textYMin, GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE)
                )
                .addGap(8)
                .addGroup(
                    createParallelGroup()
                        .addComponent(xMin, 100,  GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE)
                        .addComponent(yMin, 100,  GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE)
                )

                .addGap(30)
                .addGroup(
                    createParallelGroup()
                        .addComponent(textXMax, GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE)
                        .addComponent(textYMax, GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE)
                )
                .addGap(8)
                .addGroup(
                    createParallelGroup()
                        .addComponent(xMax, 100,  GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE)
                        .addComponent(yMax, 100,  GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE)
                )
                .addGap(30)
                .addGroup(
                    createParallelGroup()
                        .addComponent(textTMin, GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE)
                        .addComponent(textTMax, GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE)
                )
                .addGap(8)
                .addGroup(
                    createParallelGroup()
                        .addComponent(tMin, 100,  GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE)
                        .addComponent(tMax, 100,  GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE)
                )

            )
            setVerticalGroup(createSequentialGroup()
                .addGap(8)
                .addGroup(
                    createParallelGroup()
                        //.addComponent(chButton1, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(textXMin, GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE)
                        .addComponent(xMin,GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE)
                        .addComponent(textXMax, GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE)
                        .addComponent(xMax, GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE)
                        .addComponent(textTMin, GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE)
                        .addComponent(tMin, GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE)
                )
                .addGap(8)
                .addGroup(
                    createParallelGroup()
                        //.addComponent(chButton2, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(textYMin, GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE)
                        .addComponent(yMin, GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE)
                        .addComponent(textYMax, GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE)
                        .addComponent(yMax, GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE)
                        .addComponent(textTMax, GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE)
                        .addComponent(tMax, GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE)
                )
                .addGap(8)
            )

        }

        /**controlPanel.addColorListener {
        functionPainter.setColor(controlPanel.getColor1())//выбранные цвет с контрол пэнел передаем для полинома
        //derPolynomialPainter.setColor(controlPanel.getColor2())
        mainPanel.paint(mainPanel.graphics)//отрисовываем
        }*/

        xMin.addChangeListener{
            xMaxM.minimum = xMin.value as Double + 0.1
            mainPlane.xSegment = Pair(xMin.value as Double, xMax.value as Double)
            mainPanel.repaint()
        }
        xMax.addChangeListener{
            xMinM.maximum = xMax.value as Double - 0.1
            mainPlane.xSegment = Pair(xMin.value as Double, xMax.value as Double)
            mainPanel.repaint()
        }
        yMin.addChangeListener{
            yMaxM.minimum = yMin.value as Double + 0.1
            mainPlane.ySegment = Pair(yMin.value as Double, yMax.value as Double)
            mainPanel.repaint()
        }
        yMax.addChangeListener{
            yMinM.maximum = yMax.value as Double - 0.1
            mainPlane.ySegment = Pair(yMin.value as Double, yMax.value as Double)
            mainPanel.repaint()
        }

      /*mainPlane.tMax = tMaxM.value as Double
      mainPlane.tMin = tMinM.value as Double*/

      tMin.addChangeListener{
          tMaxM.minimum = tMin.value as Double + 0.1
          mainPlane.tMax = tMax.value as Double
          mainPlane.tMin = tMin.value as Double
          mainPanel.repaint()
      }
      tMax.addChangeListener{
          tMinM.maximum = tMax.value as Double - 0.1
          mainPlane.tMax = tMax.value as Double
          mainPlane.tMin = tMin.value as Double
          mainPanel.repaint()
      }

        pack()
        mainPlane.width = mainPanel.width
        mainPlane.height = mainPanel.height
    }
}