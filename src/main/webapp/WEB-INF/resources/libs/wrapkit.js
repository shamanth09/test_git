! function(a) {
    "use strict";
    var b = a.document,
        c = function() {},
        d = function(a, b) {
            for (var c in b) a[c] = b[c];
            return a
        },
        e = function(a) {
            var b = [];
            if (Array.isArray(a)) b = a;
            else if ("number" == typeof a.length)
                for (var c = 0, d = a.length; d > c; c++) b.push(a[c]);
            else b.push(a);
            return b
        },
        f = function(a, b, c) {
            $ && a.trigger(b, c)
        },
        g = function() {
            return /Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(navigator.userAgent) ? !0 : !1
        },
        h = function() {
            var c = {
                width: Math.max(b.documentElement.clientWidth, a.innerWidth || 0),
                height: Math.max(b.documentElement.clientHeight, a.innerHeight || 0)
            };
            return c
        },
        i = function(a) {
            a = "string" == typeof a ? b.querySelector(a) : a;
            var c = a.getBoundingClientRect(),
                d = {
                    top: c.top,
                    right: c.right,
                    bottom: c.bottom,
                    left: c.left
                };
            return d
        },
        j = function(a, b, c) {
            var d;
            return function() {
                var e = this,
                    f = arguments,
                    g = function() {
                        d = null, c || a.apply(e, f)
                    },
                    h = c && !d;
                clearTimeout(d), d = setTimeout(g, b), h && a.apply(e, f)
            }
        },
        k = function(a) {
            return a = "string" == typeof a ? b.querySelector(a) : a, Math.max(a.scrollHeight, a.offsetHeight, a.clientHeight)
        },
        l = function(a) {
            return a = "string" == typeof a ? b.querySelector(a) : a, Math.max(a.scrollWidth, a.offsetWidth, a.clientWidth)
        },
        m = function() {
            return Math.max(k(b.body), k(b.documentElement))
        },
        n = function() {
            return Math.max(l(b.body), l(b.documentElement))
        },
        o = function(a) {
            var c = b.createElement("script");
            c.className = "re-execute", c.type = "text/javascript", c.src = a;
            var d = b.querySelectorAll('script[src="' + a + '"]');
            d.length && [].forEach.call(d, function(a) {
                b.body.removeChild(a)
            }), b.body.appendChild(c)
        },
        p = function(a, b) {
            var c = this,
                d = b || a.css("height"),
                e = a.data();
            e = $.extend(e, {
                height: d
            }), c.isMobile() || a.slimScroll(e)
        },
        q = function(a) {
            var b = this;
            b.isMobile() || (a.slimScroll({
                destroy: !0
            }), setTimeout(function() {
                var b = a.parent();
                b.children(".slimScrollRail").remove(), b.children(".slimScrollBar").remove(), a.css({
                    overflow: "",
                    width: ""
                })
            }, 250))
        },
        r = function(a, b) {
            var c = Element.prototype;
            for (c.matches = c.matches || c.mozMatchesSelector || c.msMatchesSelector || c.oMatchesSelector || c.webkitMatchesSelector; a && !a.matches(b);) a = a.parentElement;
            return a
        },
        s = function(a) {
            var b = {
                WebkitTransition: "webkitTransitionEnd",
                MozTransition: "transitionend",
                OTransition: "oTransitionEnd otransitionend",
                transition: "transitionend"
            };
            for (var c in b)
                if (void 0 !== a.style[c]) return b[c]
        },
        t = {
            noop: c,
            extend: d,
            makeArray: e,
            jqEmiter: f,
            isMobile: g,
            viewport: h,
            offsets: i,
            debounce: j,
            getHeight: k,
            getWidth: l,
            getDocHeight: m,
            getDocWidth: n,
            createScript: o,
            initSlimScroll: p,
            destroySlimScroll: q,
            closest: r,
            transitionEnd: s
        };
    "function" == typeof define && define.amd ? define(t) : "object" == typeof exports ? module.exports = t : a.WrapkitUtils = t
}(window),
function(a, b) {
    "use strict";
    "function" == typeof define && define.amd ? define(["classie/classie", "eventEmitter/EventEmitter"], function(c, d) {
        return b(a, c, d)
    }) : "object" == typeof exports ? module.exports = b(a, require("desandro-classie"), require("wolfy87-eventemitter"), require("screenfull")) : a.WrapkitLayout = b(a, a.classie, a.EventEmitter, a.WrapkitUtils, a.screenfull)
}(window, function(a, b, c, d, e) {
    "use strict";

    function f(a, b, c) {
        return this instanceof f ? (this.elem = g.querySelector(a), i && (this.$elem = i(this.elem)), this.options = {
            box: !1,
            fullscreen: !1
        }, "function" == typeof b ? c = b : this.option(b), this._init(), void(c ? c(this.elem, this.options) : d.noop())) : new f(a, b)
    }
    var g = a.document,
        h = g.body,
        i = a.jQuery;
    return f.prototype = new c, f.prototype._init = function() {
        var a = this;
        return a.elem.dataset.initLayout ? void a.option(a.elem.dataset) : (a._box(a.options.box), a.options.fullscreen && (a.options.fullscreen = !1), a.elem.dataset.initLayout = !0, void setTimeout(function() {
            a.emit("init", a), d.jqEmiter(a.$elem, "init", a.options)
        }))
    }, f.prototype.option = function(a) {
        var b = this;
        d.extend(b.options, a), b.emit("option", b, a), d.jqEmiter(b.$elem, "option", a)
    }, f.prototype._box = function(a) {
        var c = this;
        a ? (b.add(h, "wrapkit-layout-box"), b.add(c.elem, "container")) : (b.remove(h, "wrapkit-layout-box"), b.remove(c.elem, "container")), c.options.box = a, c.emit("layoutChanged", c, a), d.jqEmiter(c.$elem, "layoutChanged", a)
    }, f.prototype._fs = function(b) {
        var c = this;
        return e.enabled ? (b ? e.request() : e.exit(), c.options.fullscreen = b, c.emit("fullscreen", c, b), d.jqEmiter(c.$elem, "fullscreen", b), void 0) : (a.alert("Your Browser does not support Fullscreen!"), !1)
    }, f.prototype.setBox = function() {
        this._box(!0)
    }, f.prototype.setFluid = function() {
        this._box(!1)
    }, f.prototype.fullscreen = function() {
        this._fs(!0)
    }, f.prototype.exitFullscreen = function() {
        this._fs(!1)
    }, i && i.bridget && i.bridget("wrapkitLayout", f), f
}),
function(a, b) {
    "use strict";
    "function" == typeof define && define.amd ? define(["classie/classie", "eventEmitter/EventEmitter"], function(c, d) {
        return b(a, c, d)
    }) : "object" == typeof exports ? module.exports = b(a, require("desandro-classie"), require("wolfy87-eventemitter")) : a.WrapkitHeader = b(a, a.classie, a.EventEmitter, a.WrapkitUtils)
}(window, function(a, b, c, d) {
    "use strict";

    function e(a, b, c) {
        return this instanceof e ? (this.elem = f.querySelector(a), h && (this.$elem = h(this.elem)), this.options = {
            skin: "default",
            fixed: !1,
            fixedPosition: "top",
            rtlMode: !1,
            prefixClass: "navbar-"
        }, "function" == typeof b ? c = b : this.option(b), this._init(), void(c ? c(this.elem, this.options) : d.noop())) : new e(a, b)
    }
    var f = a.document,
        g = f.body,
        h = a.jQuery;
    return e.prototype = new c, e.prototype._init = function() {
        var a = this;
        return a.elem.dataset.initHeader ? void a.option(a.elem.dataset) : (a.setSkin(a.options.skin), a.fixed(a.options.fixed), a.rtl(a.options.rtlMode), a.elem.dataset.initHeader = !0, void setTimeout(function() {
            a.emit("init", a), d.jqEmiter(a.$elem, "init", a.options)
        }))
    }, e.data = function() {
        return this.options
    }, e.prototype.option = function(a) {
        var b = this;
        d.extend(b.options, a), b.emit("option", b, a), d.jqEmiter(b.$elem, "option", a)
    }, e.prototype.setSkin = function(a) {
        var c = this,
            e = c.options.skin,
            f = a;
        b.remove(c.elem, c.options.prefixClass + e), b.add(c.elem, c.options.prefixClass + f), c.options.skin = f, c.emit("setSkin", c, e, f), d.jqEmiter(c.$elem, "setSkin", [e, f])
    }, e.prototype.fixed = function(c) {
        var e = this;
        c ? (b.add(e.elem, "navbar-fixed-" + e.options.fixedPosition), b.add(g, "wrapkit-header-fixed-" + e.options.fixedPosition), b.has(g, "wrapkit-sidebar-fixed") && b.remove(g.querySelector(".sidebar"), "sidebar-sticky")) : (b.remove(e.elem, "navbar-fixed-" + e.options.fixedPosition), b.remove(g, "wrapkit-header-fixed-" + e.options.fixedPosition), b.has(g, "wrapkit-sidebar-fixed") && (a.scrollY >= 50 ? b.add(g.querySelector(".sidebar"), "sidebar-sticky") : b.remove(g.querySelector(".sidebar"), "sidebar-sticky"))), e.options.fixed = c, e.emit("fixed", e, c), d.jqEmiter(e.$elem, "fixed", c)
    }, e.prototype.fixedTop = function() {
        var a = this;
        b.remove(a.elem, "navbar-fixed-top"), b.remove(a.elem, "navbar-fixed-bottom"), b.add(a.elem, "navbar-fixed-top"), b.remove(g, "wrapkit-header-fixed-top"), b.remove(g, "wrapkit-header-fixed-bottom"), b.add(g, "wrapkit-header-fixed-top"), a.options.fixedPosition = "top", a.emit("fixedTop", a), d.jqEmiter(a.$elem, "fixedTop")
    }, e.prototype.fixedBottom = function() {
        var a = this;
        b.remove(a.elem, "navbar-fixed-top"), b.remove(a.elem, "navbar-fixed-bottom"), b.add(a.elem, "navbar-fixed-bottom"), b.remove(g, "wrapkit-header-fixed-top"), b.remove(g, "wrapkit-header-fixed-bottom"), b.add(g, "wrapkit-header-fixed-bottom"), a.options.fixedPosition = "bottom", a.emit("fixedBottom", a), d.jqEmiter(a.$elem, "fixedBottom")
    }, e.prototype.rtl = function(a) {
        var c = this;
        a ? b.add(g, "wrapkit-header-rtl") : b.remove(g, "wrapkit-header-rtl"), c.options.rtlMode = a, c.emit("rtl", c), d.jqEmiter(c.$elem, "rtl")
    }, h && h.bridget && h.bridget("wrapkitHeader", e), e
}),
function(a, b) {
    "use strict";
    "function" == typeof define && define.amd ? define(["classie/classie", "eventEmitter/EventEmitter"], function(c, d) {
        return b(a, c, d)
    }) : "object" == typeof exports ? module.exports = b(a, require("desandro-classie"), require("wolfy87-eventemitter")) : a.WrapkitSidebar = b(a, a.classie, a.EventEmitter, a.interact, a.WrapkitUtils)
}(window, function(a, b, c, d, e) {
    "use strict";

    function f(a, b, c) {
        return this instanceof f ? (this.elem = g.querySelector(a), i && (this.$elem = i(this.elem)), this.options = {
            mode: "vertical",
            visible: !0,
            skin: "default",
            context: "blue",
            variant: "tabs",
            fixed: !1,
            align: "left",
            rtlMode: !1,
            size: "lg",
            resizable: !0,
            maxResize: 360,
            minResize: 220,
            caret: {
                prefix: "fa",
                collapse: "fa-angle-right",
                expand: "fa-angle-down"
            },
            loader: "fa-spinner"
        }, "function" == typeof b ? c = b : this.option(b), this._init(), void(c ? c(this.elem, this.options) : e.noop())) : new f(a, b)
    }
    var g = a.document,
        h = g.body,
        i = a.jQuery;
    return f.prototype = new c, f.prototype._init = function() {
        var c = this;
        if (c.elem.dataset.initSidebar) return void c.option(c.elem.dataset);
        c._createCaret(), c._toggleChild(), c._visibility(c.options.visible), c.resize(), c.align(c.options.align), c.rtl(c.options.rtlMode), c.size(c.options.size), c.setContext(c.options.context), c.setSkin(c.options.skin), c.setVariant(c.options.variant), c.fixed(c.options.fixed), c.setMode(c.options.mode), c._updateLayoutHeight(), e.viewport().width < 768 && c.options.visible && c.hide(), e.viewport().width >= 768 && "horizontal" === c.options.mode && !c.options.visible && c.show();
        var d = g.querySelector(".content-wrapper"),
            f = g.querySelector(".footer-wrapper");
        a.addEventListener("resize", e.debounce(function() {
            if (c._updateCaret(), e.viewport().width < 768 && (d && (d.style.paddingLeft = "", d.style.paddingRight = ""), f && (f.style.paddingLeft = "", f.style.paddingRight = "")), "horizontal" === c.options.mode && e.viewport().width >= 768) {
                var a = c.elem.querySelector(".nav-wrapper"),
                    g = a.querySelector(".nav"),
                    i = a.querySelector(".nav-fake-padding"),
                    j = getComputedStyle(g).marginLeft ? parseInt(getComputedStyle(g).marginLeft) : 0,
                    k = a.offsetWidth,
                    l = e.getWidth(g),
                    m = l - k,
                    n = c.elem.querySelectorAll(".nav-item"),
                    o = n[n.length - 1],
                    p = e.offsets(o).right + e.getWidth(o),
                    q = l - p,
                    r = b.has(h, "wrapkit-layout-box") ? m - q - e.offsets(a).left : m - q;
                j = Math.abs(j), j >= m && (j = j + Math.round(r) + 5, console.log(j), g.style.marginLeft = "-" + j + "px", j > 0 ? i.style.boxShadow = "5px 0px 5px 0px rgba(22, 24, 27, 0.156863)" : i.style.boxShadow = "");
                var s = c.elem.querySelectorAll("li.open");
                [].forEach.call(s, function(a) {
                    c._collapse(a)
                })
            }
        }, 250)), c.elem.dataset.initSidebar = !0, setTimeout(function() {
            c.emit("init", c), e.jqEmiter(c.$elem, "init", c.options)
        })
    }, f.prototype.option = function(a) {
        var b = this;
        e.extend(b.options, a), b.emit("option", b, a), e.jqEmiter(b.$elem, "option", a)
    }, f.prototype._updateLayoutHeight = function() {
        var a = this,
            b = g.querySelector(".wrapkit-wrapper"),
            c = g.querySelector(".content-wrapper");
        setTimeout(function() {
            b.style.minHeight = "", c.style.minHeight = "";
            var d = a.elem.offsetHeight,
                f = e.getHeight(a.elem),
                g = f > d;
            a.options.fixed || "vertical" !== a.options.mode || (g ? (b.style.minHeight = f + "px", c.style.minHeight = f + "px") : (b.style.minHeight = "", c.style.minHeight = ""))
        }, 250)
    }, f.prototype._expand = function(a) {
        var c = this;
        b.add(a, "open"), ("vertical" === c.options.mode || e.viewport().width < 768 && "horizontal" === c.options.mode) && (b.remove(a.querySelector(".nav-caret"), c.options.caret.collapse), b.add(a.querySelector(".nav-caret"), c.options.caret.expand)), c._updateLayoutHeight(), c.emit("_expand", c, a), e.jqEmiter(c.$elem, "_expand", a)
    }, f.prototype._collapse = function(a) {
        var c = this,
            d = a.querySelectorAll(".nav-child");
        b.remove(a, "open"), setTimeout(function() {
            ("vertical" === c.options.mode || e.viewport().width < 768 && "horizontal" === c.options.mode) && (b.remove(a.querySelector(".nav-caret"), c.options.caret.expand), b.add(a.querySelector(".nav-caret"), c.options.caret.collapse)), [].forEach.call(d, function(a) {
                b.remove(a.parentNode, "open"), ("vertical" === c.options.mode || e.viewport().width < 768 && "horizontal" === c.options.mode) && (b.remove(a.previousElementSibling.querySelector(".nav-caret"), c.options.caret.expand), b.add(a.previousElementSibling.querySelector(".nav-caret"), c.options.caret.collapse))
            })
        }, 0), c._updateLayoutHeight(), c.emit("_collapse", c, a), e.jqEmiter(c.$elem, "_collapse", a)
    }, f.prototype._toggleChild = function() {
        var a = this,
            c = a.elem.querySelectorAll('[data-toggle="nav-child"]');
        [].forEach.call(c, function(c) {
            var d = c.parentNode;
            c.addEventListener("click", function(c) {
                if (c.preventDefault(), c.stopPropagation(), "horizontal" === a.options.mode && e.viewport().width >= 768 && b.has(d, "nav-item") && !b.has(d, "open")) {
                    var f = a.elem.querySelectorAll("li.open");
                    [].forEach.call(f, function(b) {
                        a._collapse(b)
                    })
                }
                if (b.has(d, "open")) a._collapse(d);
                else {
                    var g = a.elem.querySelector(".open");
                    g && "sm" === a.options.size && b.has(d, "nav-item") && a._collapse(g), a._expand(d)
                }
            }), c.addEventListener("keyup", function(b) {
                b.preventDefault(), 27 === b.keyCode && a._collapse(d)
            }), h.addEventListener("click", function() {
                ("sm" === a.options.size || e.viewport().width >= 768 && "horizontal" === a.options.mode) && a._collapse(d)
            })
        })
    }, f.prototype._createCaret = function() {
        var a = this,
            c = a.elem.querySelectorAll('[data-toggle="nav-child"]'),
            d = "fa-angle-down";
        [].forEach.call(c, function(c) {
            var f = c.parentNode,
                h = g.createElement("i");
            b.add(h, "nav-caret"), b.add(h, "pull-right"), b.add(h, a.options.caret.prefix), "vertical" === a.options.mode ? b.has(f, "open") ? b.add(h, a.options.caret.expand) : b.add(h, a.options.caret.collapse) : b.has(f, "nav-item") ? e.viewport().width >= 768 ? b.add(h, d) : b.has(f, "open") ? b.add(h, a.options.caret.expand) : b.add(h, a.options.caret.collapse) : e.viewport().width >= 768 ? b.add(h, a.options.caret.collapse) : b.has(f, "open") ? b.add(h, a.options.caret.expand) : b.add(h, a.options.caret.collapse), c.insertBefore(h, c.firstChild)
        })
    }, f.prototype._updateCaret = function() {
        var a = this,
            b = a.elem.querySelectorAll(".nav-caret");
        [].forEach.call(b, function(a) {
            a.parentNode.removeChild(a)
        }), setTimeout(function() {
            a._createCaret()
        }, 0)
    }, f.prototype._visibility = function(a) {
        var b = this;
        a ? b.show() : b.hide()
    }, f.prototype.show = function() {
        var a = this,
            c = g.createElement("div");
        b.add(c, "sidebar-backdrop"), h.appendChild(c), c.addEventListener("click", function() {
            a.hide()
        }), b.has(h, "wrapkit-sidebar-hide") && b.remove(h, "wrapkit-sidebar-hide"), b.has(h, "wrapkit-layout-box") && (b.add(h, "push-content-front"), setTimeout(function() {
            b.remove(h, "push-content-front")
        }, 500)), a.options.visible = !0, a.emit("show", a, !0), e.jqEmiter(a.$elem, "show", !0)
    }, f.prototype.hide = function() {
        var a = this,
            c = h.querySelector(".sidebar-backdrop"),
            d = g.querySelector(".content-wrapper"),
            f = g.querySelector(".footer-wrapper");
        null !== c && h.removeChild(c), d && (d.style.paddingLeft = "", d.style.paddingRight = ""), f && (f.style.paddingLeft = "", f.style.paddingRight = ""), b.add(h, "wrapkit-sidebar-hide"), b.has(h, "wrapkit-layout-box") ? (b.add(h, "push-content-front"), setTimeout(function() {
            b.remove(h, "push-content-front")
        }, 500)) : a.options.fixed || a.elem.setAttribute("style", ""), a.options.visible = !1, a.emit("hide", a, !1), e.jqEmiter(a.$elem, "hide", !1)
    }, f.prototype.horizontalControl = function() {
        var c = this,
            d = c.elem.querySelector(".nav-wrapper"),
            f = d.querySelector(".nav"),
            h = g.createElement("div"),
            i = g.createElement("div"),
            j = g.createElement("a"),
            k = g.createElement("a"),
            l = g.createElement("span"),
            m = g.createElement("span");
        b.add(h, "nav-ctrl"), b.add(h, "btn-group"), b.add(i, "nav-fake-padding"), b.add(i, "bg-" + c.options.skin), b.add(j, "btn"), b.add(j, "hover-" + c.options.context), b.add(j, "btn-icon"), b.add(j, "btn-xs"), b.add(k, "btn"), b.add(k, "hover-" + c.options.context), b.add(k, "btn-icon"), b.add(k, "btn-xs"), b.add(l, "icon-arrow-left"), b.add(m, "icon-arrow-right"), j.appendChild(l), k.appendChild(m), h.appendChild(j), h.appendChild(k), d.appendChild(h), d.appendChild(i), setTimeout(function() {
            e.getWidth(f) > d.offsetWidth ? (b.remove(h, "hide"), b.remove(i, "hide"), d.style.boxShadow = "inset -10px 0 5px -5px rgba(22, 24, 27, 0.16)") : (b.add(h, "hide"), b.add(i, "hide"), d.style.boxShadow = "")
        }, 250);
        var n = 0,
            o = 200;
        k.addEventListener("click", function() {
            var a = d.offsetWidth,
                b = e.getWidth(f),
                c = b - a;
            n += o, n = n > c ? c : n, f.style.marginLeft = "-" + n + "px", n > 0 ? i.style.boxShadow = "5px 0px 5px 0px rgba(22, 24, 27, 0.156863)" : i.style.boxShadow = ""
        }), j.addEventListener("click", function() {
            var a = 0;
            n = n > a ? n - o : a, n = a > n ? a : n, f.style.marginLeft = "-" + n + "px", n > 0 ? i.style.boxShadow = "5px 0px 5px 0px rgba(22, 24, 27, 0.156863)" : i.style.boxShadow = ""
        }), a.addEventListener("resize", e.debounce(function() {
            "horizontal" === c.options.mode && (e.viewport().width < 768 ? (b.add(c.elem.querySelector(".nav-wrapper .nav"), "nav-stacked"), b.add(h, "hide"), b.add(i, "hide"), f.style.marginLeft = "", d.style.boxShadow = "", i.style.boxShadow = "") : (b.remove(c.elem.querySelector(".nav-wrapper .nav"), "nav-stacked"), c.options.visible || c.show(), e.getWidth(f) > d.offsetWidth ? (b.remove(h, "hide"), b.remove(i, "hide"), d.style.boxShadow = "inset -10px 0 5px -5px rgba(22, 24, 27, 0.16)") : (b.add(h, "hide"), b.add(i, "hide"), d.style.boxShadow = "")))
        }, 0))
    }, f.prototype.destroyHorizontalControl = function() {
        var a = this,
            b = a.elem.querySelector(".nav-wrapper"),
            c = b.querySelector(".nav"),
            d = b.querySelector(".nav-ctrl"),
            e = b.querySelector(".nav-fake-padding");
        null !== d && null !== e && (d.parentElement.removeChild(d), e.parentElement.removeChild(e), b.style.boxShadow = "", c.style.marginLeft = "")
    }, f.prototype.updateHorizontalControl = function() {
        var a = this;
        a.destroyHorizontalControl(), a.horizontalControl()
    }, f.prototype.setMode = function(a) {
        var c = this,
            d = c.elem.querySelector(".nav-wrapper .nav");
        if (c.options.rtlMode && c.rtl(!1), "horizontal" === a) {
            var f = c.elem.querySelectorAll("li.open");
            [].forEach.call(f, function(a) {
                c._collapse(a)
            })
        }
        c._updateCaret(), c.updateHorizontalControl(), "horizontal" === a ? (b.remove(h, "wrapkit-sidebar-vertical"), b.remove(d, "nav-stacked"), b.add(h, "wrapkit-sidebar-horizontal"), c.options.resizable && (b.add(c.elem.querySelector(".sidebar-resize-handler"), "hide"), c.elem.style.width = ""), e.viewport().width < 768 ? (b.add(c.elem.querySelector(".nav-wrapper .nav"), "nav-stacked"), c.elem.querySelector(".nav-wrapper .nav").style.marginLeft = "") : (b.remove(c.elem.querySelector(".nav-wrapper .nav"), "nav-stacked"), c.elem.style.width = "")) : (b.remove(h, "wrapkit-sidebar-horizontal"), b.add(h, "wrapkit-sidebar-vertical"), b.add(d, "nav-stacked"), c.options.resizable && b.remove(c.elem.querySelector(".sidebar-resize-handler"), "hide")), c.options.mode = a, c.emit("setMode", c, a), e.jqEmiter(c.$elem, "setMode", a)
    }, f.prototype.resize = function() {
        var a = this,
            c = "left" === this.options.align ? ".sidebar-resize-handler" : !1,
            f = "right" === this.options.align ? ".sidebar-resize-handler" : !1,
            h = g.createElement("div");
        b.add(h, "sidebar-resize-handler"), null !== a.elem.querySelector(".sidebar-resize-handler") && a.elem.querySelector(".sidebar-resize-handler").parentNode.removeChild(a.elem.querySelector(".sidebar-resize-handler")), a.options.resizable && a.elem.appendChild(h), d(a.elem).resizable({
            edges: {
                left: f,
                right: c,
                bottom: !1,
                top: !1
            }
        }).on("resizemove", function(b) {
            var c = b.target,
                d = parseFloat(c.getAttribute("data-x")) || 0,
                e = b.rect.width;
            e = e < a.options.minResize ? a.options.minResize : e, e = e > a.options.maxResize ? a.options.maxResize : e, c.style.width = e + "px", c.style.opacity = .8, c.style.webkitTransform = c.style.transform = "translateX(" + d + "px)", c.setAttribute("data-x", d), c.setAttribute("data-cw", e), b.rect.width = e
        }).on("resizeend", function(b) {
            var c = b.target,
                d = 768,
                f = parseFloat(c.getAttribute("data-cw")),
                h = g.querySelector(".content-wrapper"),
                i = g.querySelector(".footer-wrapper");
            c.style.opacity = 1, e.viewport().width >= d && ("left" === a.options.align ? (h && (h.style.paddingLeft = f + "px"), i && (i.style.paddingLeft = f + "px")) : (h && (h.style.paddingRight = f + "px"), i && (i.style.paddingRight = f + "px"))), a.emit("resize", a, f), e.jqEmiter(a.$elem, "resize", f)
        })
    }, f.prototype.resizable = function(a) {
        var c, d = this;
        "horizontal" !== d.options.mode && (a ? (c = g.createElement("div"), b.add(c, "sidebar-resize-handler"), d.elem.appendChild(c)) : (c = g.querySelector(".sidebar-resize-handler"), d.elem.removeChild(c)), d.options.resizable = a, d.emit("resizable", d, a), e.jqEmiter(d.$elem, "resizable", a))
    }, f.prototype.isResizable = function() {
        return this.options.resizable
    }, f.prototype.align = function(a) {
        var c = this;
        b.remove(h, "wrapkit-sidebar-left"), b.remove(h, "wrapkit-sidebar-right"), b.add(h, "wrapkit-sidebar-" + a);
        var d = c.elem.querySelector(".nav");
        b.remove(d, "nav-left"), b.remove(d, "nav-right"), b.add(d, "nav-" + a), c.options.align = a, c.resize();
        var f = c.elem.dataset.cw,
            i = g.querySelector(".content-wrapper"),
            j = g.querySelector(".footer-wrapper");
        f && "lg" === c.options.size && ("left" === a ? (i && (i.style.paddingLeft = f + "px", i.style.paddingRight = ""), j && (j.style.paddingLeft = f + "px", j.style.paddingRight = "")) : (i && (i.style.paddingRight = f + "px", i.style.paddingLeft = ""), j && (j.style.paddingRight = f + "px", j.style.paddingLeft = ""))), c.emit("align", c, a), e.jqEmiter(c.$elem, "align", a)
    }, f.prototype.rtl = function(c) {
        var d = this,
            f = d.elem.querySelector(".nav");
        if (c) {
            if ("horizontal" === d.options.mode) return void a.alert("The rtl direction is not support for horizontal sidebar!");
            b.add(h, "wrapkit-sidebar-rtl"), b.add(f, "nav-rtl")
        } else b.remove(h, "wrapkit-sidebar-rtl"), b.remove(f, "nav-rtl");
        d.options.rtlMode = c, d.emit("rtl", d, c), e.jqEmiter(d.$elem, "rtl", c)
    }, f.prototype.size = function(a) {
        var c = this;
        if (b.remove(h, "wrapkit-sidebar-lg"), b.remove(h, "wrapkit-sidebar-sm"), b.add(h, "wrapkit-sidebar-" + a), "sm" === a) {
            var d = g.querySelector(".content-wrapper"),
                f = g.querySelector(".footer-wrapper");
            d && (d.style.paddingLeft = "", d.style.paddingRight = ""), f && (f.style.paddingLeft = "", f.style.paddingRight = ""), c.elem.style.width = ""
        }
        c.options.fixed && setTimeout(function() {
            c.fixed(!0)
        }, 250), c.updateHorizontalControl(), c.options.size = a, c.emit("size", c, a), e.jqEmiter(c.$elem, "size", a)
    }, f.prototype.setContext = function(a) {
        var c = this,
            d = this.options.context,
            f = a,
            h = "tabs" === this.options.variant ? "nav-contrast-" : "nav-",
            i = g.querySelector(".nav"),
            j = i.querySelectorAll(".nav-child");
        b.remove(i, "nav-contrast-" + d), b.remove(i, "nav-" + d), b.add(i, h + f), [].forEach.call(j, function(a) {
            b.remove(a, "nav-" + d), b.add(a, "nav-" + f)
        }), c.options.context = f, c.emit("setContext", c, d, f), e.jqEmiter(c.$elem, "setContext", [d, f])
    }, f.prototype.setSkin = function(a) {
        var c = this,
            d = c.options.skin,
            f = a;
        b.remove(c.elem, "sidebar-" + d), b.add(c.elem, "sidebar-" + f), c.options.skin = f, c.updateHorizontalControl(), c.emit("setSkin", c, d, f), e.jqEmiter(c.$elem, "setSkin", [d, f])
    }, f.prototype.setVariant = function(a) {
        var c = this,
            d = c.options.variant,
            f = a,
            h = g.querySelector(".nav");
        b.remove(h, "nav-" + d), b.add(h, "nav-" + f), c.options.variant = f, c.setContext(c.options.context), c.emit("setVariant", c, d, f), e.jqEmiter(c.$elem, "setVariant", [d, f])
    }, f.prototype.fixed = function(c) {
        var d = this,
            f = d.elem.querySelector(".nav-wrapper"),
            i = function() {
                var a = e.viewport().height,
                    b = e.offsets(f).top,
                    c = a - b;
                "vertical" === d.options.mode || e.viewport().width < 768 && "horizontal" === d.options.mode ? f.style.height = c + "px" : f.style.height = ""
            },
            j = function() {
                b.has(h, "wrapkit-header-fixed-top") || (a.scrollY > e.offsets(d.elem).top ? b.add(d.elem, "sidebar-sticky") : b.remove(d.elem, "sidebar-sticky"), e.debounce(i, 250), d.emit("sticky"), e.jqEmiter(d.$elem, "sticky"))
            },
            k = e.debounce(function() {
                d.options.fixed && "sm" === d.options.size && ("vertical" === d.options.mode || e.viewport().width < 768 && "horizontal" === d.options.mode) && (d.size("lg"), d.fakeSize = !0)
            }, 250),
            l = e.debounce(function() {
                if (d.fakeSize && ("vertical" === d.options.mode || e.viewport().width < 768 && "horizontal" === d.options.mode)) {
                    d.size("sm");
                    var a = d.elem.querySelector("li.open");
                    a && d._collapse(a), d.fakeSize = !1
                }
            }, 250);
        if (c) {
            var m = g.querySelector(".wrapkit-wrapper"),
                n = g.querySelector(".content-wrapper");
            m.style.minHeight = "", n.style.minHeight = "", b.add(h, "wrapkit-sidebar-fixed"), j(), a.addEventListener("scroll", j), a.addEventListener("resize", e.debounce(i, 250)), d.elem.addEventListener("mouseenter", k), d.elem.addEventListener("mouseleave", l), i()
        } else setTimeout(function() {
            d._updateLayoutHeight(), f.style.height = ""
        }, 250), b.remove(h, "wrapkit-sidebar-fixed"), a.removeEventListener("scroll", j), a.removeEventListener("resize", e.debounce(i, 250)), d.elem.removeEventListener("mouseenter", k), d.elem.removeEventListener("mouseleave", l);
        d.elem.querySelector(".sidebar-block") && (d.elem.querySelector(".sidebar-block").style.display = "none", setTimeout(function() {
            d.elem.querySelector(".sidebar-block").style.display = ""
        }, 0)), d.options.fixed = c, d.emit("fixed", d, c), e.jqEmiter(d.$elem, "fixed", c)
    }, f.prototype.showLoader = function(a, c) {
        var d = this,
            f = d.elem.querySelector(".nav"),
            h = a - 1,
            i = f.querySelectorAll(".nav-item"),
            j = i[h],
            k = j.querySelector(".nav-icon"),
            l = g.createElement("span");
        c || (c = d.options.loader), b.add(l, "fa"), b.add(l, "fa-spin"), b.add(l, c), b.add(j, "loader-state-show"), b.add(k, "nav-loader"), k.appendChild(l), d.options.loader = c, d.emit("showLoader", d, l, j), e.jqEmiter(d.$elem, "showLoader", [l, j])
    }, f.prototype.hideLoader = function(a) {
        var c = this,
            d = c.elem.querySelector(".nav"),
            f = a - 1,
            g = d.querySelectorAll(".nav-item"),
            h = g[f],
            i = h.querySelector(".nav-icon"),
            j = i.querySelector("." + c.options.loader);
        i.removeChild(j), b.remove(h, "loader-state-show"), b.remove(i, "nav-loader"), c.emit("hideLoader", c, c.options.loader, h), e.jqEmiter(c.$elem, "hideLoader", [c.options.loader, h])
    }, i && i.bridget && i.bridget("wrapkitSidebar", f), f
}),
function(a, b) {
    "use strict";
    "function" == typeof define && define.amd ? define(["classie/classie", "eventEmitter/EventEmitter"], function(c, d) {
        return b(a, c, d)
    }) : "object" == typeof exports ? module.exports = b(a, require("desandro-classie"), require("wolfy87-eventemitter")) : a.WrapkitContent = b(a, a.classie, a.EventEmitter, a.WrapkitUtils)
}(window, function(a, b, c, d) {
    "use strict";

    function e(a, b, c) {
        return this instanceof e ? (this.elem = f.querySelector(a), h && (this.$elem = h(this.elem)), this.options = {
            rtlMode: !1
        }, "function" == typeof b ? c = b : this.option(b), this._init(), void(c ? c(this.elem, this.options) : d.noop())) : new e(a, b)
    }
    var f = a.document,
        g = f.body,
        h = a.jQuery;
    return e.prototype = new c, e.prototype._init = function() {
        var a = this;
        return a.elem.dataset.initContent ? void a.option(a.elem.dataset) : (a.rtl(a.options.rtlMode), a.elem.dataset.initContent = !0, void setTimeout(function() {
            a.emit("init", a), d.jqEmiter(a.$elem, "init", a.options)
        }))
    }, e.prototype.option = function(a) {
        var b = this;
        d.extend(b.options, a), b.emit("option", b, a), d.jqEmiter(b.$elem, "option", a)
    }, e.prototype.rtl = function(a) {
        var c = this;
        a ? b.add(g, "wrapkit-content-rtl") : b.remove(g, "wrapkit-content-rtl"), c.options.rtlMode = a, c.emit("rtl", c), d.jqEmiter(c.$elem, "rtl")
    }, h && h.bridget && h.bridget("wrapkitContent", e), e
}),
function(a, b) {
    "use strict";
    "function" == typeof define && define.amd ? define(["classie/classie", "eventEmitter/EventEmitter"], function(c, d) {
        return b(a, c, d)
    }) : "object" == typeof exports ? module.exports = b(a, require("desandro-classie"), require("wolfy87-eventemitter")) : a.WrapkitFooter = b(a, a.classie, a.EventEmitter, a.WrapkitUtils)
}(window, function(a, b, c, d) {
    "use strict";

    function e(a, b, c) {
        return this instanceof e ? (this.elem = f.querySelector(a), h && (this.$elem = h(this.elem)), this.options = {
            prefixClass: "footer-",
            skin: "default",
            rtlMode: !1
        }, "function" == typeof b ? c = b : this.option(b), this._init(), void(c ? c(this.elem, this.options) : d.noop())) : new e(a, b)
    }
    var f = a.document,
        g = f.body,
        h = a.jQuery;
    return e.prototype = new c, e.prototype._init = function() {
        var a = this;
        return a.elem.dataset.initFooter ? void a.option(a.elem.dataset) : (a.setSkin(a.options.skin), a.rtl(a.options.rtlMode), a.elem.dataset.initFooter = !0, void setTimeout(function() {
            a.emit("init", a), d.jqEmiter(a.$elem, "init", a.options)
        }))
    }, e.prototype.option = function(a) {
        var b = this;
        d.extend(b.options, a), b.emit("option", b, a), d.jqEmiter(b.$elem, "option", a)
    }, e.prototype.setSkin = function(a) {
        var c = this,
            e = c.options.skin,
            f = a;
        b.remove(c.elem, c.options.prefixClass + e), b.add(c.elem, c.options.prefixClass + f), c.options.skin = f, c.emit("setSkin", c, e, f), d.jqEmiter(c.$elem, "setSkin", [e, f])
    }, e.prototype.rtl = function(a) {
        var c = this;
        a ? b.add(g, "wrapkit-footer-rtl") : b.remove(g, "wrapkit-footer-rtl"), c.options.rtlMode = a, c.emit("rtl", c), d.jqEmiter(c.$elem, "rtl")
    }, h && h.bridget && h.bridget("wrapkitFooter", e), e
}),
function(a, b) {
    "use strict";
    "function" == typeof define && define.amd ? define(["classie/classie", "eventEmitter/EventEmitter"], function(c, d) {
        return b(a, c, d)
    }) : "object" == typeof exports ? module.exports = b(a, require("desandro-classie"), require("wolfy87-eventemitter")) : a.WrapkitPanel = b(a, a.classie, a.EventEmitter, a.WrapkitUtils)
}(window, function(a, b, c, d) {
    "use strict";

    function e(a, b, c) {
        return this instanceof e ? (this.elem = "string" == typeof a ? f.querySelector(a) : a, h && (this.$elem = h(a)), this.options = {
            collapse: !1,
            expand: !1,
            loaderTemplate: '<i class="fa fa-spin fa-spinner"></i>',
            context: "default",
            loaderColor: "blue",
            fillColor: !1
        }, "function" == typeof b ? c = b : this.option(b), this._init(), void(c ? c(this.elem, this.options) : d.noop())) : new e(a, b)
    }
    var f = a.document,
        g = f.body,
        h = a.jQuery;
    return e.prototype = new c, e.prototype._init = function() {
        var a = this;
        if (a.elem.dataset.initPanel) return void a.option(a.elem.dataset);
        b.add(a.elem, "fade"), b.add(a.elem, "in"), a.collapse(a.options.collapse), a.expand(a.options.expand), a.setContext(a.options.context);
        var c = a.elem.querySelectorAll('[data-toggle="panel-collapse"]'),
            e = a.elem.querySelectorAll('[data-toggle="panel-hide"]'),
            f = a.elem.querySelectorAll('[data-toggle="panel-expand"]'),
            g = a.elem.querySelectorAll('[data-toggle="panel-context"]'),
            h = a.elem.querySelectorAll('[data-toggle="panel-fill"]'),
            i = a.elem.querySelectorAll('[data-toggle="panel-refresh"]');
        [].forEach.call(c, function(b) {
            b.addEventListener("click", function(b) {
                b.preventDefault(), a.collapse(!a.options.collapse)
            })
        }), [].forEach.call(e, function(b) {
            b.addEventListener("click", function(b) {
                b.preventDefault(), a.hide()
            })
        }), [].forEach.call(f, function(c) {
            c.addEventListener("click", function(c) {
                c.preventDefault();
                var d;
                a.expand(!a.options.expand), a.options.expand ? (d = this.querySelector(".arrow_expand"), null !== d && (b.add(d, "arrow_condense"), b.remove(d, "arrow_expand"))) : (d = this.querySelector(".arrow_condense"), null !== d && (b.remove(d, "arrow_condense"), b.add(d, "arrow_expand"))), a.options.collapse && a.collapse(!1)
            })
        }), [].forEach.call(g, function(b) {
            b.addEventListener("click", function(c) {
                c.preventDefault();
                var d = b.dataset,
                    e = d.context || "default",
                    f = "true" === d.fill || !1;
                a.setContext(e), a.setFill(f)
            })
        }), [].forEach.call(h, function(b) {
            b.addEventListener("click", function(c) {
                c.preventDefault();
                var d = b.dataset,
                    e = d.fill || !1;
                a.setFill(e)
            }), b.addEventListener("change", function(c) {
                c.preventDefault();
                var d = b.checked;
                a.setFill(d)
            })
        }), [].forEach.call(i, function(b) {
            b.addEventListener("click", function(b) {
                b.preventDefault(), a.showLoader()
            })
        }), a.elem.dataset.initPanel = !0, setTimeout(function() {
            a.emit("init", a), d.jqEmiter(a.$elem, "init", a.options)
        })
    }, e.prototype.option = function(a) {
        var b = this;
        d.extend(b.options, a), b.emit("option", b, a), d.jqEmiter(b.$elem, "option", a)
    }, e.prototype.setContext = function(a) {
        var c = this,
            e = c.options.context,
            f = a;
        b.has(c.elem, "panel-" + e) && b.remove(c.elem, "panel-" + e), b.add(c.elem, "panel-" + f), c.setFill(c.options.fillColor), c.options.context = a, c.emit("setContext", c, a), d.jqEmiter(c.$elem, "setContext", a)
    }, e.prototype.setFill = function(a) {
        var c = this,
            e = "true" === a || a === !0;
        e ? b.add(c.elem, "panel-fill") : b.remove(c.elem, "panel-fill"), c.options.fillColor = e, c.emit("toggleFill", c, e), d.jqEmiter(c.$elem, "toggleFill", e)
    }, e.prototype.hide = function() {
        var a = this;
        b.remove(a.elem, "in");
        var c = d.transitionEnd(a.elem);
        a.options.expand && (g.style.overflow = "", a.elem.removeAttribute("style")), a.elem.addEventListener(c, function() {
            b.add(a.elem, "hide")
        }), a.emit("hide", a), d.jqEmiter(a.$elem, "hide")
    }, e.prototype.collapse = function(a) {
        var c = this;
        a ? b.add(c.elem, "panel-collapsed") : b.remove(c.elem, "panel-collapsed"), c.options.collapse = a, c.emit("collapse", c, a), d.jqEmiter(c.$elem, "collapse", a)
    }, e.prototype.expand = function(a) {
        var c = this;
        a ? (b.add(c.elem, "panel-expanded"), g.style.overflow = "hidden") : (b.remove(c.elem, "panel-expanded"), g.style.overflow = ""), c.options.expand = a, c.emit("expand", c, a), d.jqEmiter(c.$elem, "expand", a)
    }, e.prototype.showLoader = function() {
        var a = this,
            c = a.elem.querySelector(".panel-heading"),
            e = a.elem.querySelector(".panel-title"),
            g = f.createElement("div"),
            h = f.createElement("div"),
            i = f.createElement("div");
        b.has(a.elem, "panel-onloading") || (b.add(g, "panel-loader"), b.add(h, "loader-inner"), b.add(h, "ball-clip-rotate"), b.add(i, "border-" + a.options.loaderColor), i.style.cssText = "border-bottom-color:transparent !important;width:20px;height:20px;border-radius:10px;", h.appendChild(i), g.appendChild(h), c.appendChild(g), b.add(e, "hide"), b.add(a.elem, "panel-onloading"), a.emit("showLoader", a), d.jqEmiter(a.$elem, "showLoader"))
    }, h && h.bridget && h.bridget("wrapkitPanel", e), e
});