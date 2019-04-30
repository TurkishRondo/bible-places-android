package com.turkishrondo.www.bibleplaces

//----------------------------------------------------------------------------------------------------------------------
// SingletonHolder
//
// This is a generic singleton class whose getInstance() method takes one argument (A), which is a context, and returns
// the singleton(T) instance.  A "doubled-checked locking" algorithm is used to synchronize in order for this to be
// thread-safe.  Derived classes should have the following format:
//
// class Derived private constructor(context: Context)
// {
//     init
//     {
//         // Init using context argument
//     }
//
//     companion object : SingletonHolder<Manager, Context>(::Manager)
// }
//----------------------------------------------------------------------------------------------------------------------
open class SingletonHolder<out T, in A>(creator: (A) -> T)
{
    private var mCreator: ((A) -> T)? = creator

    // @Volatile is needed in order for the algorithm to work properly.
    @Volatile private var mInstance: T? = null

    fun getInstance(arg: A): T
    {
        val i = mInstance
        if (i != null)
        {
            return i
        }

        return synchronized(this)
        {
            val i2 = mInstance
            if (i2 != null)
            {
                i2
            }
            else
            {
                val created = mCreator!!(arg)
                mInstance = created
                mCreator = null
                created
            }
        }
    }
}